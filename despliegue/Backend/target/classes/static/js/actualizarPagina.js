
const eventSource = new EventSource('/sse');

eventSource.onmessage = () => {
    location.reload(); // Recargar la página
};
