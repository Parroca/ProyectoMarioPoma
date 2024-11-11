document.addEventListener('DOMContentLoaded', (event) => {
    document.querySelectorAll('.toggle-password').forEach(item => {
        item.addEventListener('click', function () {
            const passwordField = document.querySelector(this.getAttribute('toggle'));
            const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordField.setAttribute('type', type);
            this.classList.toggle('bi-eye');
            this.classList.toggle('bi-eye-slash');
        });
    });
});

const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirm-password');
const fullName = document.querySelector('input[placeholder="Full Name"]');
const email = document.querySelector('input[placeholder="Email"]');
const form = document.querySelector('form');
const meter = document.getElementById('password-strength-meter');
const text = document.getElementById('password-strength-text');
const strength = {
    0: "Worst ☹",
    1: "Bad ☹",
    2: "Weak ☹",
    3: "Good ☺",
    4: "Strong ☻"
};

password.addEventListener('input', function() {
    const val = password.value;
    const result = zxcvbn(val);

    meter.value = result.score;

    if (val !== "") {
        meter.style.display = 'block';
        text.style.display = 'block';

        text.innerHTML = "Strength: " + "<strong>" + strength[result.score] + "</strong>" + "<span class='feedback'>" + result.feedback.warning + " " + result.feedback.suggestions + "</span>";

        if (result.score >= 2) {
            confirmPassword.disabled = false;
        } else {
            confirmPassword.disabled = true;
            confirmPassword.value= '';
        }

    } else {
        meter.style.display = 'none';
        text.style.display = 'none';
    }
});

confirmPassword.addEventListener('input', function() {
    if (confirmPassword.value !== password.value) {
        confirmPassword.setCustomValidity("Passwords do not match");
    } else {
        confirmPassword.setCustomValidity("");
    }
});

fullName.addEventListener('input', function() {
    const nameRegex = /^[a-zA-Z\s]+$/;
    if (!nameRegex.test(fullName.value)) {
        fullName.setCustomValidity("Full Name cannot contain numbers or special characters");
    } else {
        fullName.setCustomValidity("");
    }
});

// Validar email para que siga la estructura correcta
email.addEventListener('input', function() {
    const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
    if (!emailRegex.test(email.value)) {
        email.setCustomValidity("Invalid email format");
    } else {
        email.setCustomValidity("");
    }
});

form.addEventListener('submit', function(event) {
    if (!fullName.checkValidity() || !email.checkValidity() || !password.checkValidity() || !confirmPassword.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
        form.classList.add('was-validated');
    }
});

const images = [
    '/images/back/bg.jpg',
    '/images/back/kyoto.jpg',
    '/images/back/sb.jpg',
    '/images/back/bg2.jpg',
    '/images/back/bl.jpg',
];

let currentIndex = 0;
const transitionDuration = 1500; 

function changeBackgroundImage() {
    document.body.style.transition = `background-image ${transitionDuration}ms ease-in-out`;
    currentIndex = (currentIndex + 1) % images.length;
    document.body.style.backgroundImage = `url(${images[currentIndex]})`;
}

setInterval(changeBackgroundImage, 5000); 