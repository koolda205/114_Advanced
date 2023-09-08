"use strict";

const url = "http://localhost:8081/api/users/"

async function getAdminPage() {
    let page = await fetch(url);
    if (page.ok) {
        let listAllUser = await page.json();
        loadTableData(listAllUser);
    } else {
        alert(`Error, ${page.status}`)
    }
}

const pills = document.querySelectorAll('.pill');
const pillsContent = document.querySelectorAll('.pillContent');
pills.forEach((clickedPill) => {
    clickedPill.addEventListener('click', async () => {
        pills.forEach((pill) => {
            pill.classList.remove('active');
        });
        clickedPill.classList.add('active');
        let tabId = clickedPill.getAttribute('id');
        await activePillContent(tabId);
    });
});

async function activePillContent(tabId) {
    pillsContent.forEach((clickedPillContent) => {
        clickedPillContent.classList.contains(tabId) ?
            clickedPillContent.classList.add('active') :
            clickedPillContent.classList.remove('active');
    })
}

async function getMyUser() {
    let res = await fetch('/api/auth');
    let resUser = await res.json();
    userNavbarDetails(resUser);
}

window.addEventListener('DOMContentLoaded', getMyUser);

function userNavbarDetails(resUser) {
    let userList = document.getElementById('myUserDetails');
    let roles = ''
    for (let role of resUser.roles) {
        roles += role.userRole + ' '
    }
    userList.insertAdjacentHTML('beforeend', `
        <b> ${resUser.email} </b> with roles: <a>${roles} </a>`);
}

function loadTableData(listAllUser) {
    let tableBody = document.getElementById('tbody');
    let dataHtml = '';
    for (let user of listAllUser) {
        let roles = [];
        for (let role of user.roles) {
            roles.push(" " + role.userRole)
        }
        dataHtml +=
            `<tr>
    <td>${user.id}</td>
    <td>${user.name}</td>
    <td>${user.surname}</td>
    <td>${user.age}</td>
    <td>${user.email}</td>
    <td>${roles}</td>
    <td>
        <button class="btn blue-background" data-bs-toogle="modal"
        data-bs-target="#editModal"
        onclick="editModalData(${user.id})">Edit</button>
    </td>
        <td>
        <button class="btn btn-danger" data-bs-toogle="modal"
        data-bs-target="#deleteModal"
        onclick="deleteModalData(${user.id})">Delete</button>
    </td>
</tr>`
    }
    tableBody.innerHTML = dataHtml;
}

getAdminPage();



async function loadUserTable() {
    let tableBody = document.getElementById('tableUser');
    let page = await fetch("/api/auth");
    let currentUser;
    if (page.ok) {
        currentUser = await page.json();
    } else {
        alert(`Error, ${page.status}`)
    }
    let dataHtml = '';
    let roles = [];
    for (let role of currentUser.roles) {
        roles.push(" " + role.userRole)
    }
    dataHtml +=
        `<tr>
    <td>${currentUser.id}</td>
    <td>${currentUser.name}</td>
    <td>${currentUser.surname}</td>
    <td>${currentUser.age}</td>
    <td>${currentUser.email}</td>
    <td>${roles}</td>
</tr>`
    tableBody.innerHTML = dataHtml;
}

const tabs = document.querySelectorAll('.taba');
const tabsContent = document.querySelectorAll('.tabaContent');
tabs.forEach((clickedTab) => {
    clickedTab.addEventListener('click', async () => {
        tabs.forEach((tab) => {
            tab.classList.remove('active');
        });
        clickedTab.classList.add('active');
        let tabaId = clickedTab.getAttribute('id');
        await activeTabContent(tabaId);
    });
});

async function activeTabContent(tabaId) {
    tabsContent.forEach((clickedTabContent) => {
        clickedTabContent.classList.contains(tabaId) ?
            clickedTabContent.classList.add('active') :
            clickedTabContent.classList.remove('active');
    })
}

const form_new = document.getElementById('formForNewUser');

async function newUser() {
    form_new.addEventListener('submit', addNewUser);
}

async function addNewUser(event) {
    event.preventDefault();
    let listOfRole = [];
    for (let i = 0; i < form_new.roleSelect.options.length; i++) {
        if (form_new.roleSelect.options[i].selected) {
            listOfRole.push({id: form_new.roles.options[i].value,
            role: form_new.roles.options[i].text});
        }
    }
    let method = {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: form_new.name.value,
            surname: form_new.surname.value,
            age: form_new.age.value,
            email: form_new.email.value,
            password: form_new.password.value,
            roles: listOfRole
        })
    }
    await fetch(url, method).then(() => {
        form_new.reset();
        getAdminPage();
        activeTabContent('home-tab');
        let activateTab = document.getElementById('home-tab');
        activateTab.classList.add('active');
        let deactivateTab = document.getElementById('profile-tab');
        deactivateTab.classList.remove('active');
    });
}

const form_ed = document.getElementById('formForEditing');
const id_ed = document.getElementById('id_ed');
const name_ed = document.getElementById('name_ed');
const surname_ed = document.getElementById('surname_ed');
const age_ed = document.getElementById('age_ed');
const email_ed = document.getElementById('email_ed');
const password_ed = document.getElementById('password_ed');
// const role_ed = document.getElementById('role_ed');

async function editModalData(id) {
    $('#editModal').modal('show');
    const urlDataEd = url + id;
    let usersPageEd = await fetch(urlDataEd);
    if (usersPageEd.ok) {
        await usersPageEd.json().then(user => {
            id_ed.value = `${user.id}`;
            name_ed.value = `${user.name}`;
            surname_ed.value = `${user.surname}`;
            age_ed.value = `${user.age}`;
            email_ed.value = `${user.email}`;
            password_ed.value = `${user.password}`;
        })
    } else {
        alert(`Error, ${usersPageEd.status}`)
    }
}

async function editUser() {
    let urlEdit = url + id_ed.value;
    let listOfRole = [];
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            listOfRole.push({id: form_ed.roles.options[i].value,
                userRole: form_ed.roles.options[i].text});
        }
    }
    let method = {
        method: 'PATCH',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: form_ed.editedUserId.value,
            name: form_ed.name.value,
            surname: form_ed.surname.value,
            age: form_ed.age.value,
            email: form_ed.email.value,
            password: form_ed.password.value,
            roles: listOfRole
        })
    }
    await fetch(urlEdit, method).then(() => {
        $('#editCloseBtn').click();
        getAdminPage();
    })
}

const form_del = document.getElementById('formForDeleting');
const id_del = document.getElementById('id_del');
const name_del = document.getElementById('name_del');
const surname_del = document.getElementById('surname_del');
const age_del = document.getElementById('age_del');
const email_del = document.getElementById('email_del');

async function deleteModalData(id) {
    $('#deleteModal').modal('show');
    const urlForDel = url + id;
    let usersPageDel = await fetch(urlForDel);
    if (usersPageDel.ok) {
        await usersPageDel.json().then(user => {
            id_del.value = `${user.id}`;
            name_del.value = `${user.name}`;
            surname_del.value = `${user.surname}`;
            age_del.value = `${user.age}`;
            email_del.value = `${user.email}`;
        })
    } else {
        alert(`Error, ${usersPageDel.status}`)
    }
}

async function deleteUser() {
    let urlDel = url + id_del.value;
    let listOfRole = [];
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            listOfRole.push({id: form_ed.roles.options[i].value,
                userRole: form_ed.roles.options[i].text});
        }
    }
    let method = {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: form_del.name.value,
            surname: form_del.surname.value,
            age: form_del.age.value,
            email: form_del.email.value,
            roles: listOfRole
        })
    }
    await fetch(urlDel, method).then(() => {
        $('#deleteCloseBtn').click();
        getAdminPage();
    })
}



