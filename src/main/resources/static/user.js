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
            roles += role.name + ' '
        }
        userList.insertAdjacentHTML('beforeend', `
        <b> ${resUser.email} </b> with roles: <a>${roles} </a>`);

    }
window.addEventListener('DOMContentLoaded', loadUserTable);
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
            roles.push(" " + role.name)
        }
        dataHtml +=
            `<tr>
    <td>${currentUser.id}</td>
    <td>${currentUser.username}</td>
    <td>${currentUser.surname}</td>
    <td>${currentUser.age}</td>
    <td>${currentUser.email}</td>
    <td>${roles}</td>
</tr>`
        tableBody.innerHTML = dataHtml;
    }
