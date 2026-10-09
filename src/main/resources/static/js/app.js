const API_URL = "/api/produtos";

const form = document.getElementById("product-form");
const productIdInput = document.getElementById("product-id");
const nameInput = document.getElementById("nome");
const categoryInput = document.getElementById("categoria");
const basePriceInput = document.getElementById("preco-base");

const submitButton = document.getElementById("submit-button");
const cancelEditButton = document.getElementById("cancel-edit-button");
const refreshButton = document.getElementById("refresh-button");

const formTitle = document.getElementById("form-title");
const tableBody = document.getElementById("products-table-body");
const message = document.getElementById("message");

document.addEventListener("DOMContentLoaded", loadProducts);

refreshButton.addEventListener("click", loadProducts);

cancelEditButton.addEventListener("click", resetForm);

form.addEventListener("submit", async (event) => {

    event.preventDefault();

    const productId = productIdInput.value;

    const payload = {
        nome: nameInput.value.trim(),
        categoria: categoryInput.value,
        preco_base: Number(basePriceInput.value)
    };

    try {

        if (productId) {

            await updateProduct(
                productId,
                payload
            );

        } else {

            await createProduct(payload);
        }

        resetForm();
        await loadProducts();

    } catch (error) {

        showMessage(
            error.message,
            "error"
        );
    }
});

async function loadProducts() {

    tableBody.innerHTML = `
        <tr>
            <td colspan="5" class="empty-state">
                Carregando produtos...
            </td>
        </tr>
    `;

    try {

        const response =
            await fetch(API_URL);

        if (!response.ok) {
            throw new Error(
                "Não foi possível carregar os produtos."
            );
        }

        const products =
            await response.json();

        renderProducts(products);

    } catch (error) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="empty-state">
                    Erro ao carregar produtos.
                </td>
            </tr>
        `;

        showMessage(
            error.message,
            "error"
        );
    }
}

function renderProducts(products) {

    if (products.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="empty-state">
                    Nenhum produto cadastrado.
                </td>
            </tr>
        `;

        return;
    }

    tableBody.innerHTML =
        products
            .map(product => `
                <tr>

                    <td>
                        ${escapeHtml(product.nome)}
                    </td>

                    <td>
                        ${escapeHtml(product.categoria)}
                    </td>

                    <td>
                        ${formatCurrency(product.preco_base)}
                    </td>

                    <td>
                        ${formatCurrency(product.preco_tarifado)}
                    </td>

                    <td>
                        <button
                            class="edit-button"
                            type="button"
                            data-id="${product.id}"
                        >
                            Editar
                        </button>
                    </td>

                </tr>
            `)
            .join("");

    document
        .querySelectorAll(".edit-button")
        .forEach(button => {

            button.addEventListener(
                "click",
                () => {

                    const product =
                        products.find(
                            item =>
                                item.id === button.dataset.id
                        );

                    startEditing(product);
                }
            );
        });
}

async function createProduct(payload) {

    const response =
        await fetch(
            API_URL,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(payload)
            }
        );

    const body =
        await readResponse(response);

    if (!response.ok) {
        throw new Error(
            formatApiError(body)
        );
    }

    showMessage(
        `Produto "${body.nome}" cadastrado com sucesso. Preço tarifado: ${formatCurrency(body.preco_tarifado)}.`,
        "success"
    );
}

async function updateProduct(
    productId,
    payload
) {

    const response =
        await fetch(
            `${API_URL}/${productId}`,
            {
                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(payload)
            }
        );

    const body =
        await readResponse(response);

    if (!response.ok) {
        throw new Error(
            formatApiError(body)
        );
    }

    showMessage(
        `Produto "${body.nome}" atualizado com sucesso. Preço tarifado: ${formatCurrency(body.preco_tarifado)}.`,
        "success"
    );
}

function startEditing(product) {

    productIdInput.value =
        product.id;

    nameInput.value =
        product.nome;

    categoryInput.value =
        product.categoria;

    basePriceInput.value =
        product.preco_base;

    formTitle.textContent =
        "Editar produto";

    submitButton.textContent =
        "Salvar alterações";

    cancelEditButton.classList.remove(
        "hidden"
    );

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

function resetForm() {

    form.reset();

    productIdInput.value = "";

    formTitle.textContent =
        "Cadastrar produto";

    submitButton.textContent =
        "Cadastrar produto";

    cancelEditButton.classList.add(
        "hidden"
    );
}

function showMessage(
    text,
    type
) {

    message.textContent = text;

    message.className =
        `message ${type}`;

    window.setTimeout(
        () => {
            message.classList.add(
                "hidden"
            );
        },
        5000
    );
}

function formatCurrency(value) {

    return new Intl.NumberFormat(
        "pt-BR",
        {
            style: "currency",
            currency: "BRL"
        }
    ).format(value);
}

async function readResponse(response) {

    const contentType =
        response.headers.get(
            "content-type"
        );

    if (
        contentType &&
        contentType.includes(
            "application/json"
        )
    ) {

        return response.json();
    }

    return {};
}

function formatApiError(body) {

    if (body.erro) {
        return body.erro;
    }

    const validationErrors =
        Object.values(body);

    if (validationErrors.length > 0) {
        return validationErrors.join(" | ");
    }

    return "Não foi possível concluir a operação.";
}

function escapeHtml(value) {

    const element =
        document.createElement("div");

    element.textContent =
        value ?? "";

    return element.innerHTML;
}