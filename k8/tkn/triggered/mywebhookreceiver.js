const express = require('express');
const app = express();
//const port = 3030;
const port = 80;

app.use(express.json());

//app.post('/webhook', (req, res) => {
app.post('/', (req, res) => {
  console.log('Webhook Payload:', req.body);
  res.status(200).send('Webhook received successfully');
});

app.listen(port, () => {
  //console.log(`Webhook receiver running at http://localhost/webhook:${port}`);
  console.log(`Webhook receiver running at http://bankofantarc.com:${port}`);
});

