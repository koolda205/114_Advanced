const url = 'http://localhost:8080/api/users/'
const container = document.querySelector('tbody')
let result = ''

const modalArticul = new bootstrap.Modal(document.getElementById('modalArticul'))
const formArticul = document.querySelector('form')
const name = document.getElementById('name')
const surname = document.getElementById('surname')
const age= document.getElementById('age')
const email= document.getElementById('email')
const password= document.getElementById('password')
const role= document.getElementById('role')
let option = ''

btnCreate.addEventListener('click', () => {
    description.value = ''
    price.value = ''
    stock.value = ''
    modalArticul.show()
    option = 'create'
})

const display = (users) => {
    users.forEach(user => {
        result += `<tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${user.password}</td>
                        <td>${user.role}</td>
                        <td class="text center"><a class="btnEdit btn btn-primary">Edit<a/>
                        <a class="btnDelete btn btn-danger">Delete<a/></td>
                    </tr>
                    `
    });
    container.innerHTML = result
}

fetch(url)
    .then(response => response.json())
    .then(data => display(data))
    .catch(error => console.log(error))