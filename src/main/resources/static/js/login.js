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
