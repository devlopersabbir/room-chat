const socket = new WebSocket("ws://localhost:5000");

const sendMessage = (event) => {
  event.preventDefault();
  const input = document.querySelector("input");

  if (input.value) {
    socket.send(input.value);
    input.value = "";
  }
  input.focus({
    preventScroll: true,
  });
};

document.querySelector("form").addEventListener("submit", sendMessage);

// listen message
socket.addEventListener("message", (event) => {
  const li = document.createElement("li");
  li.textContent = event.data;
  document.querySelector("ul").appendChild(li);
});
