const express = require('express');
const app = express();

app.post('/webhook', (req, res) => {
  console.log('Webhook received:', req.body); // Print the webhook payload to the console
  res.status(200).send('Webhook received successfully!'); // Respond to the webhook request
});

const port = 3000; // Choose a port number
app.listen(port, () => {
  console.log(`Webhook server listening on port ${port}`);
});
