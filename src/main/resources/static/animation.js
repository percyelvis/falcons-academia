// MENU HAMBURGUESA
const hamb = document.getElementById("hamburguesa");
const nav = document.getElementById("nav");

hamb.onclick = () => {
    nav.classList.toggle("active");
};

// ANIMACIONES (NO HERO)
const animar = document.querySelectorAll(".animar");

window.addEventListener("scroll", () => {
    animar.forEach(el=>{
        let pos = el.getBoundingClientRect().top;
        if(pos < window.innerHeight - 100){
            el.style.opacity = 1;
            el.style.transform = "translateY(0)";
        }
    });
});

animar.forEach(el=>{
    el.style.opacity = 0;
    el.style.transform = "translateY(40px)";
    el.style.transition = "0.6s";
});

// ===============================
// PREMATRICULA (FORMULARIO)
// ===============================
const form = document.getElementById("formPrematricula");

if(form){
    form.addEventListener("submit", function(e){
        e.preventDefault();

        const data = {
            nombres: document.getElementById("nombres").value,
            apellidos: document.getElementById("apellidos").value,
            dni: document.getElementById("dni").value,
            celular: document.getElementById("celular").value,
            correo: document.getElementById("correo").value,
            ciclo: document.getElementById("ciclo").value
        };

        fetch("/prematricula/guardar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(res => {
                if(res.ok){
                    alert("Pre-matrícula enviada correctamente");
                    form.reset();
                } else {
                    alert("Error al enviar datos");
                }
            })
            .catch(err => {
                console.error(err);
                alert("Error de conexión con el servidor");
            });

    });
}

// ---------------------------
// Mensaje de confirmación prematrícula
// ---------------------------
window.addEventListener('DOMContentLoaded', () => {
    const mensaje = document.getElementById('mensaje-ok'); // div del mensaje
    if (mensaje && mensaje.classList.contains('visible')) {
        // Espera 3 segundos antes de desvanecer
        setTimeout(() => {
            mensaje.style.opacity = '0'; // fade out
            setTimeout(() => {
                mensaje.classList.remove('visible');
                mensaje.classList.add('hidden'); // oculta completamente
            }, 500); // tiempo de transición coincide con CSS
        }, 3000); // 3 segundos visibles
    }
});

function mostrar(tipo){
    document.getElementById("online").style.display = "none";
    document.getElementById("fisico").style.display = "none";

    document.getElementById(tipo).style.display = "block";
}

// ONLINE
function pagoOnline(){
    document.getElementById("msg-online").innerText =
        "⏳ Pago en revisión. Te notificaremos por WhatsApp 📲";
}

// CODIGO
function generarCodigo(){
    let codigo = "FAL-" + Math.floor(Math.random()*999999);
    document.getElementById("codigo").innerText = codigo;
}

// PDF
function descargarPDF(){
    let elemento = document.getElementById("fisico");

    html2pdf().from(elemento).save("orden_pago_falcons.pdf");
}