$(document).ready(function () {
    $('.datatable').DataTable({
        stripeClasses: ['odd', 'even'], // Estilo alternante
        language: {
            url: '//cdn.datatables.net/plug-ins/1.11.5/i18n/es-ES.json'
        }
    });
});