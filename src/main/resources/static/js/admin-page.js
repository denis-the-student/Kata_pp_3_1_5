const successMessage = document.getElementById('success-message');
const successMessageText = document.getElementById('success-message-text');
const failMessage = document.getElementById('fail-message');
const failMessageText = document.getElementById('fail-message-text');
const editModalFailMessage = document.getElementById('edit-modal-fail-message');
const editModalFailMessageText = document.getElementById('edit-modal-fail-message-text');


// ------------------------- All users table body -------------------------------------------------------

const allUsersTableBody = document.getElementById('all-users-table-body');

refreshUsersTable()
    .catch(error => {
        console.error(error);
    });

// ------------------------- Function to refresh users table --------------------------------------------

async function refreshUsersTable() {
    try {
        const response = await fetch('/api/users');
        const users = await response.json();

        allUsersTableBody.innerHTML = '';

        users.forEach(({id, username, email, roles}) => {
            const row = allUsersTableBody.insertRow();
            row.innerHTML = `
                <tr>
                  <td>${id}</td>
                  <td>${username}</td>
                  <td>${email}</td>
                  <td>${roles.map(role => role.name.split("_")[1]).join(', ')}</td>
                  
                  <td><button class="btn btn-info" data-toggle="modal" data-target="#edit-modal" 
                          data-userid="${id}">Edit</button></td>
                          
                  <td><button class="btn btn-danger" data-toggle="modal" data-target="#delete-modal" 
                          data-userid="${id}">Delete</button></td>
                </tr>
            `;

            const editButton = row.querySelector('.btn-info');
            editButton.addEventListener('click', () => {
                renderEditModal(id, username, email, roles);
            });

            const deleteButton = row.querySelector('.btn-danger');
            deleteButton.addEventListener('click', () => {
                renderDeleteModal(id, username, email, roles);
            });

        });

    } catch (error) {
        console.error(error);
    }
}


// ------------------------- Function to render edit modal ---------------------------------------------

function renderEditModal(id, username, email, roles) {
    const modalBody = document.getElementById('edit-user-modal-body');
    modalBody.innerHTML = `
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="id-edit">ID</label>
      <input class="form-control" id="id-edit" name="id" type="text" value="${id}" readonly>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="username-edit">Username</label>
      <input class="form-control" id="username-edit" name="username" type="text" value="${username}" minlength="2" required>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="email-edit">Email</label>
      <input class="form-control" id="email-edit" name="email" type="email" value="${email}" required>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="password-edit">Password</label>
      <input class="form-control" id="password-edit" name="password" type="password" minlength="4" required>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="roles-edit">Role</label>
      <select class="form-control" name="roles" size="2" id="roles-edit" multiple required>
        <!-- All roles with selected roles of current user -->
      </select>
    </div>
  `;

    const modalFooter = document.getElementById('edit-user-modal-footer');
    modalFooter.innerHTML = `
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
    <button type="button" class="btn btn-primary" id="confirmEdit">Edit</button>
  `;

    const editButton = modalFooter.querySelector('.btn-primary');
    editButton.addEventListener('click', () => {
        updateUser(id)
            .then(response => {
                console.log(response);

                successMessage.style.display = 'block';
                successMessage.classList.add('d-inline-block');
                successMessageText.textContent = `Хуман с id=${id} обновлён.`;

                refreshUsersTable().then();
                $('#edit-modal').modal('hide');

                failMessage.style.display = 'none';
                failMessage.classList.remove('d-inline-block');
                editModalFailMessage.style.display = 'none';

            })
            .catch((error) => {
                console.error(error);

                editModalFailMessage.style.display = 'block';
                editModalFailMessageText.textContent = error;

                successMessage.style.display = 'none';
                successMessage.classList.remove('d-inline-block');
            });
    });

    fetchRolesAndRenderOptions(roles);
}


// ------------------------- Function to update user ----------------------------------------------------

async function updateUser(id) {
    const user = {
        username: document.getElementById(`username-edit`).value,
        email: document.getElementById(`email-edit`).value,
        password: document.getElementById(`password-edit`).value,
        roles: Array.from(document.getElementById(`roles-edit`).selectedOptions)
            .map(role => ({id: role.value}))
    };

    const requestOptions = {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    };

    const response = await fetch(`/api/users/${id}`, requestOptions);
    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message);
    }

    return response.json();
}


// ------------------------- Function to render delete modal ---------------------------------------------

function renderDeleteModal(userId, username, email, roles) {
    const modalBody = document.getElementById('delete-user-modal-body');
    modalBody.innerHTML = `
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="id-delete">ID</label>
      <input class="form-control" id="id-delete" type="text" value="${userId}" readonly>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="username-delete">Username</label>
      <input class="form-control" id="username-delete" type="text" value="${username}" readonly>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="email-delete">Email</label>
      <input class="form-control" id="email-delete" type="text" value="${email}" readonly>
    </div>
    
    <div class="form-group col-8 mx-auto">
      <label class="col-form-label" for="roles-delete">Role</label>
      <select class="form-control" name="roles" size="2" id="roles-delete" multiple disabled>
        <!-- All roles with selected roles of current user -->
      </select>
    </div>
  `;

    const modalFooter = document.getElementById('delete-user-modal-footer');
    modalFooter.innerHTML = `
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
    <button type="button" class="btn btn-danger" id="confirmDelete">Delete</button>
  `;

    const deleteButton = modalFooter.querySelector('.btn-danger');
    deleteButton.addEventListener('click', () => {
        deleteUser(userId)
            .then(response => {
                console.log(response);

                successMessage.style.display = 'block';
                successMessage.classList.add('d-inline-block');
                successMessageText.textContent = `Хуман ${username} с id=${userId} удалён из базы.`;

                refreshUsersTable().then();
                $('#delete-modal').modal('hide');

                failMessage.style.display = 'none';
                failMessage.classList.remove('d-inline-block');

            })
            .catch((error) => {
                console.error(error);

                failMessage.style.display = 'block';
                failMessage.classList.add('d-inline-block');
                failMessageText.textContent = error;

                successMessage.style.display = 'none';
                successMessage.classList.remove('d-inline-block');

                $('#delete-modal').modal('hide');
            });
    });

    fetchRolesAndRenderOptions(roles);
}


// ------------------------- Function to delete user ----------------------------------------------------

async function deleteUser(userId) {
    const requestOptions = {
        method: 'DELETE'
    };

    const response = await fetch(`/api/users/${userId}`, requestOptions);
    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message);
    }

    return response.statusText;
}


// ------------------------- Function to render role options ---------------------------------------------

function renderRoleOptions(userRoles, allRoles) {
    const selectElements = document.querySelectorAll('#roles-edit, #roles-delete');

    selectElements.forEach(selectElement => {
        allRoles.forEach(role => {
            const optionElement = document.createElement('option');
            optionElement.value = role.id;
            optionElement.textContent = role.name.split("_")[1];
            optionElement.selected = userRoles.some(userRole => userRole.id === role.id);
            selectElement.appendChild(optionElement);
        });
    });
}

function fetchRolesAndRenderOptions(userRoles) {
    fetch('/api/roles')
        .then(response => response.json())
        .then(data => {
            renderRoleOptions(userRoles, data);
        })
        .catch(error => console.error(error));
}


// ------------------------- Adding new user -------------------------------------------------------------

document.getElementById('add-new-user-btn').addEventListener('click', function (event) {
    event.preventDefault();

    const username = document.getElementById('new-username').value;
    const email = document.getElementById('new-email').value;
    const password = document.getElementById('new-password').value;
    const roles = Array.from(document.getElementById('new-roles').selectedOptions)
        .map(role => ({id: role.value}));

    addUser(username, email, password, roles)
        .then(response => {
            console.log(response);

            successMessage.style.display = 'block';
            successMessage.classList.add('d-inline-block');
            successMessageText.textContent = `Хуман ${username} добавлен в базу.`;

            refreshUsersTable().then(document.querySelector('#all-users-tab').click());

            failMessage.style.display = 'none';
            failMessage.classList.remove('d-inline-block');

        })
        .catch(error => {
            console.error(error);
            failMessage.style.display = 'block';
            failMessage.classList.add('d-inline-block');
            failMessageText.textContent = error;

            successMessage.style.display = 'none';
            successMessage.classList.remove('d-inline-block');
        });
});


// ------------------------- Function to add new user ----------------------------------------------------

async function addUser(username, email, password, roles) {
    const user = {
        username: username,
        email: email,
        password: password,
        roles: roles
    };

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    };

    const response = await fetch('/api/users', requestOptions);
    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message);
    }

    return response.json();
}


// ------------------------- Get all roles --------------------------------------------------------------

fetch('/api/roles')
    .then(response => response.json())
    .then(data => {
        const selectElement = document.getElementById('new-roles');
        data.forEach(role => {
            const optionElement = document.createElement('option');
            optionElement.value = role.id;
            optionElement.textContent = role.name.split("_")[1];
            selectElement.appendChild(optionElement);
        });
    })
    .catch(error => console.error(error));
