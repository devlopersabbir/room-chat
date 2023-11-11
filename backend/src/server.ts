import { Server } from "ws";

const server = new Server({ port: 5000 });

server.on("connection", (socket) => {
  socket.on("message", (message) => {
    console.log("message: ", message.toString());
    socket.send(`${message}`);
  });
});

console.log("server running!");
