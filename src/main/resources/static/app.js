const LOW_STOCK_LIMIT = 4;

const api = {
    products: "/api/products",
    orders: "/api/orders",
    dashboard: "/api/analytics/dashboard"
};

const money = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD"
});

const productForm = document.getElementById("productForm");
const orderForm = document.getElementById("orderForm");
const productsElement = document.getElementById("products");
const lowStockProductsElement = document.getElementById("lowStockProducts");
const ordersElement = document.getElementById("orders");
const orderProduct = document.getElementById("orderProduct");
const categoryFilter = document.getElementById("categoryFilter");
const lowStockOnly = document.getElementById("lowStockOnly");
const customerSearch = document.getElementById("customerSearch");
const orderStatusTabs = document.getElementById("orderStatusTabs");
const messageElement = document.getElementById("message");
const productRefreshButton = document.getElementById("productRefreshButton");
const refreshButton = document.getElementById("refreshButton");
const pageRefreshButton = document.getElementById("pageRefreshButton");

let products = [];
let activeOrderStatus = "";
let searchTimer;

productForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const product = {
        name: document.getElementById("productName").value,
        category: document.getElementById("productCategory").value,
        price: Number(document.getElementById("productPrice").value),
        stock: Number(document.getElementById("productStock").value)
    };

    await postJson(api.products, product);
    productForm.reset();
    showMessage("Product created.");
    await loadPage();
});

orderForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const order = {
        customerName: document.getElementById("customerName").value,
        items: [
            {
                productId: Number(orderProduct.value),
                quantity: Number(document.getElementById("orderQuantity").value)
            }
        ]
    };

    await postJson(api.orders, order);
    orderForm.reset();
    document.getElementById("orderQuantity").value = 1;
    showMessage("Order reserved.");
    await loadPage();
});

productRefreshButton.addEventListener("click", async () => {
    await loadPage();
    showMessage("Products refreshed.");
});

refreshButton.addEventListener("click", async () => {
    await loadPage();
    showMessage("Orders refreshed.");
});

pageRefreshButton.addEventListener("click", async () => {
    await loadPage();
    showMessage("Dashboard refreshed.");
});

categoryFilter.addEventListener("change", loadPage);
lowStockOnly.addEventListener("change", loadPage);

customerSearch.addEventListener("input", () => {
    clearTimeout(searchTimer);
    searchTimer = setTimeout(loadPage, 250);
});

orderStatusTabs.addEventListener("click", async (event) => {
    const tab = event.target.closest("button[data-status]");

    if (!tab) {
        return;
    }

    activeOrderStatus = tab.dataset.status;
    document.querySelectorAll(".tab").forEach((button) => button.classList.remove("active"));
    tab.classList.add("active");
    await loadPage();
});

ordersElement.addEventListener("click", async (event) => {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const action = button.dataset.action;
    const orderId = button.dataset.orderId;

    await postJson(`${api.orders}/${orderId}/${action}`);
    showMessage(actionMessage(action));
    await loadPage();
});

productsElement.addEventListener("submit", restockFromForm);
lowStockProductsElement.addEventListener("submit", restockFromForm);

async function loadPage() {
    const [dashboard, loadedProducts, orders, categories, lowStockProducts] = await Promise.all([
        getJson(api.dashboard),
        getJson(productUrl()),
        getJson(orderUrl()),
        getJson(`${api.products}/categories`),
        getJson(`${api.products}/low-stock?limit=${LOW_STOCK_LIMIT}`)
    ]);

    products = loadedProducts;
    renderDashboard(dashboard);
    renderCategoryFilter(categories);
    renderProducts(products);
    renderProductSelect(products);
    renderLowStockProducts(lowStockProducts);
    renderOrders(orders);
}

function productUrl() {
    const params = new URLSearchParams();

    if (categoryFilter.value) {
        params.set("category", categoryFilter.value);
    }

    if (lowStockOnly.checked) {
        params.set("lowStockOnly", "true");
        params.set("lowStockLimit", String(LOW_STOCK_LIMIT));
    }

    return params.toString() ? `${api.products}?${params}` : api.products;
}

function orderUrl() {
    const params = new URLSearchParams();
    const customerName = customerSearch.value.trim();

    if (activeOrderStatus) {
        params.set("status", activeOrderStatus);
    }

    if (customerName) {
        params.set("customerName", customerName);
    }

    return params.toString() ? `${api.orders}?${params}` : api.orders;
}

function renderDashboard(dashboard) {
    document.getElementById("reservedOrders").textContent = dashboard.reservedOrders;
    document.getElementById("paidOrders").textContent = dashboard.paidOrders;
    document.getElementById("invoicedOrders").textContent = dashboard.invoicedOrders;
    document.getElementById("cancelledOrders").textContent = dashboard.cancelledOrders;
    document.getElementById("revenue").textContent = money.format(dashboard.revenue);
    document.getElementById("totalProducts").textContent = dashboard.totalProducts;
    document.getElementById("lowStockProductsCount").textContent = dashboard.lowStockProducts;
    document.getElementById("totalOrders").textContent = dashboard.totalOrders;
    document.getElementById("averageOrderValue").textContent = money.format(dashboard.averageOrderValue);
    document.getElementById("bestSellingProduct").textContent = dashboard.bestSellingProduct.name;
    document.getElementById("bestSellingQuantity").textContent = `${dashboard.bestSellingProduct.quantity} sold`;
}

function renderCategoryFilter(categories) {
    const selectedCategory = categoryFilter.value;
    const options = [`<option value="">All categories</option>`]
        .concat(categories.map((category) => `
            <option value="${escapeHtml(category)}">${escapeHtml(category)}</option>
        `));

    categoryFilter.innerHTML = options.join("");
    categoryFilter.value = selectedCategory;
}

function renderProducts(items) {
    if (items.length === 0) {
        productsElement.innerHTML = `<div class="empty">No matching products.</div>`;
        return;
    }

    productsElement.innerHTML = items.map((product) => `
        <div class="table-row product-row">
            <strong>${escapeHtml(product.name)}</strong>
            <span>${escapeHtml(product.category)}</span>
            <span>${money.format(product.price)}</span>
            <span class="badge ${stockClass(product.stock)}">${product.stock} in stock</span>
            ${renderRestockForm(product)}
        </div>
    `).join("");
}

function renderProductSelect(items) {
    const availableProducts = items.filter((product) => product.stock > 0);

    if (availableProducts.length === 0) {
        orderProduct.innerHTML = `<option value="">No products in stock</option>`;
        orderProduct.disabled = true;
        return;
    }

    orderProduct.disabled = false;
    orderProduct.innerHTML = availableProducts.map((product) => `
        <option value="${product.id}">${escapeHtml(product.name)} (${product.stock} left)</option>
    `).join("");
}

function renderLowStockProducts(items) {
    if (items.length === 0) {
        lowStockProductsElement.innerHTML = `<div class="empty">No low-stock products.</div>`;
        return;
    }

    lowStockProductsElement.innerHTML = items.map((product) => `
        <div class="compact-row">
            <div>
                <strong>${escapeHtml(product.name)}</strong>
                <small>${escapeHtml(product.category)} - ${money.format(product.price)}</small>
            </div>
            <div class="compact-actions">
                <span class="badge LOW_STOCK">${product.stock} left</span>
                ${renderRestockForm(product)}
            </div>
        </div>
    `).join("");
}

function renderRestockForm(product) {
    return `
        <form class="restock-form" data-product-id="${product.id}">
            <label>
                Qty
                <input name="quantity" type="number" min="1" step="1" value="5" required>
            </label>
            <button class="small-button" type="submit">Restock</button>
        </form>
    `;
}

async function restockFromForm(event) {
    const form = event.target.closest(".restock-form");

    if (!form) {
        return;
    }

    event.preventDefault();

    const quantity = Number(new FormData(form).get("quantity"));
    const productId = form.dataset.productId;

    await postJson(`${api.products}/${productId}/restock`, { quantity });
    showMessage("Product restocked.");
    await loadPage();
}

function renderOrders(items) {
    if (items.length === 0) {
        ordersElement.innerHTML = `<div class="empty">No matching orders.</div>`;
        return;
    }

    ordersElement.innerHTML = [...items].reverse().map((order) => `
        <div class="table-row order-row">
            <div>
                <strong>#${order.id} - ${escapeHtml(order.customerName)}</strong>
                <small>${order.items.length} item(s)</small>
            </div>
            <span>${money.format(order.total)}</span>
            <span class="badge ${order.status}">${formatStatus(order.status)}</span>
            <div class="row-actions">
                ${renderOrderActions(order)}
            </div>
        </div>
    `).join("");
}

function renderOrderActions(order) {
    if (order.status === "RESERVED") {
        return `
            <button class="small-button" data-action="pay" data-order-id="${order.id}" type="button">Mark Paid</button>
            <button class="small-button danger-button" data-action="cancel" data-order-id="${order.id}" type="button">Cancel</button>
        `;
    }

    if (order.status === "PAID") {
        return `<button class="small-button" data-action="invoice" data-order-id="${order.id}" type="button">Create Invoice</button>`;
    }

    return `<span class="quiet-text">No action</span>`;
}

function actionMessage(action) {
    if (action === "pay") {
        return "Order marked as paid.";
    }

    if (action === "invoice") {
        return "Invoice created.";
    }

    return "Order cancelled and stock returned.";
}

async function getJson(url) {
    const response = await fetch(url);
    return handleResponse(response);
}

async function postJson(url, body) {
    const options = {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    };

    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    return handleResponse(response);
}

async function handleResponse(response) {
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Request failed");
    }

    return data;
}

function showMessage(text) {
    messageElement.textContent = text;
    setTimeout(() => {
        messageElement.textContent = "";
    }, 4500);
}

function stockClass(stock) {
    return stock <= LOW_STOCK_LIMIT ? "LOW_STOCK" : "";
}

function formatStatus(status) {
    return status.charAt(0) + status.slice(1).toLowerCase();
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

window.addEventListener("unhandledrejection", (event) => {
    showMessage(event.reason.message);
});

loadPage();
