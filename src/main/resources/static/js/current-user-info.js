// ------------------------- Navbar current user info --------------------------------------------------------------

fetch('/api/users/current-user')
    .then(response => response.json())
    .then(data => {
        const currentUserName = data.email;
        const currentUserRoles = data.roles.map(role => role.name.split("_")[1]).join(', ');
        document.querySelector('#currentUserEmail').innerText = currentUserName;
        document.querySelector('#currentUserRoles').innerText = currentUserRoles;
    })
    .catch(error => console.error(error));


// ------------------------- Current user table body ---------------------------------------------------------------

fetch('/api/users/current-user')
    .then(response => response.json())
    .then(currentUser => {
        const tableBody = document.querySelector('#current-user-table-body');
        tableBody.innerHTML = '';

        const row = `
          <tr>
                <td>${currentUser.id}</td>
                <td>${currentUser.username}</td>
                <td>${currentUser.email}</td>
                <td>${currentUser.roles.map(role => role.name.split("_")[1]).join(', ')}</td>
          </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    })
    .catch(error => console.error(error));