document.addEventListener('DOMContentLoaded', function () {
    fetch('/user', {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(user => {
            document.getElementById('user-id').innerText = user.id;
            document.getElementById('user-name').innerText = user.name;
            document.getElementById('user-surname').innerText = user.surname;
            document.getElementById('user-age').innerText = user.age;
            document.getElementById('user-email').innerText = user.email;
            document.getElementById('user-roles').innerText = user.roles.map(role => role.replace('ROLE_', '')).join(', ');

            // Заполняем данные в navbar
            document.getElementById('navbar-user-email').innerText = user.email;
            document.getElementById('navbar-user-roles').innerText = user.roles.map(role => role.replace('ROLE_', '')).join(', ');
        })
        // .catch(error => {
        //     console.error('Ошибка:', error);
        // });
});
