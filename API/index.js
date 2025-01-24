const express = require('express');
const bodyParser = require('body-parser');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const mysql = require('mysql2'); // Import mysql2

const app = express();
const PORT = 3000;

app.use(bodyParser.json());

// Create a MySQL connection
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',         // Change to your MySQL username
  password: '',         // Change to your MySQL password
  database: 'db_catering'     // Use the existing database
});

// Connect to the MySQL server and select the database
db.connect((err) => {
  if (err) {
    console.error('Error connecting to the MySQL server:', err);
    process.exit(1);
  }
  console.log('Connected to the MySQL database.');
});

const JWT_SECRET = 'sdfsdfsdgsdgsgvwetxcrtwrwbi86om7';

// Register API
app.post('/register', async (req, res) => {
    const { username, email, password } = req.body;

    if (!username || !email || !password) {
        return res.status(400).json({ message: 'Username, email, and password are required.' });
    }

    // Check if the user or email already exists in the database
    const [rows] = await db.promise().query('SELECT * FROM users WHERE username = ? OR email = ?', [username, email]);
    if (rows.length > 0) {
        return res.status(400).json({ message: 'Username or email already exists.' });
    }

    // Hash the password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Insert user into the database
    db.promise().query('INSERT INTO users (username, email, password) VALUES (?, ?, ?)', [username, email, hashedPassword])
        .then(() => {
            res.status(201).json({ message: 'User registered successfully.' });
        })
        .catch(err => {
            console.error('Error inserting user into the database:', err);
            res.status(500).json({ message: 'Internal server error.' });
        });
});

// Login API
app.post('/login', async (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({ message: 'Username and password are required.' });
    }

    // Retrieve user from the database
    const [rows] = await db.promise().query('SELECT * FROM users WHERE username = ?', [username]);
    const user = rows[0];
    if (!user) {
        return res.status(400).json({ message: 'Invalid username or password.' });
    }

    // Compare the password
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
        return res.status(400).json({ message: 'Invalid username or password.' });
    }

    // Generate JWT token
    const token = jwt.sign({ username }, JWT_SECRET, { expiresIn: '1h' });
    res.status(200).json({ message: 'Login successful.', token });
});

// Middleware to verify JWT token from Authorization header
const authenticateToken = (req, res, next) => {
    const token = req.headers['authorization'] && req.headers['authorization'].split(' ')[1];
    if (!token) {
        return res.status(401).json({ message: 'Token is required.' });
    }

    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).json({ message: 'Unauthorized: Invalid token.' });
        }
        req.user = decoded;
        next();
    });
};

// Create Order API
app.post('/order', authenticateToken, async (req, res) => {
    const { orderDetails } = req.body;

    if (!orderDetails || orderDetails.length === 0) {
        return res.status(400).json({ message: 'Order details are required.' });
    }

    try {
        // Insert order into the database
        const orderQuery = 'INSERT INTO orders (username, order_details) VALUES (?, ?)';
        db.promise().query(orderQuery, [req.user.username, JSON.stringify(orderDetails)])
            .then(([results]) => {
                res.status(201).json({ message: 'Order placed successfully.', orderId: results.insertId });
            })
            .catch(err => {
                console.error('Error placing order:', err);
                res.status(500).json({ message: 'Internal server error.' });
            });

    } catch (err) {
        console.error('Error processing order:', err);
        res.status(500).json({ message: 'Internal server error.' });
    }
});

// Read Orders (GET) - For a specific user
app.get('/orders', authenticateToken, async (req, res) => {
    try {
        // Retrieve orders for the user
        const [orders] = await db.promise().query('SELECT * FROM orders WHERE username = ?', [req.user.username]);
        res.status(200).json({ orders });

    } catch (err) {
        console.error('Error fetching orders:', err);
        res.status(500).json({ message: 'Internal server error.' });
    }
});

// Update Order (PUT)
app.put('/order/:id', authenticateToken, async (req, res) => {
    const { id } = req.params;
    const { orderDetails } = req.body;

    if (!orderDetails || orderDetails.length === 0) {
        return res.status(400).json({ message: 'Order details are required.' });
    }

    try {
        // Update the order in the database
        const updateQuery = 'UPDATE orders SET order_details = ? WHERE id = ? AND username = ?';
        db.promise().query(updateQuery, [JSON.stringify(orderDetails), id, req.user.username])
            .then(([results]) => {
                if (results.affectedRows > 0) {
                    res.status(200).json({ message: 'Order updated successfully.' });
                } else {
                    res.status(404).json({ message: 'Order not found or you are not authorized to update this order.' });
                }
            })
            .catch(err => {
                console.error('Error updating order:', err);
                res.status(500).json({ message: 'Internal server error.' });
            });

    } catch (err) {
        console.error('Error processing update:', err);
        res.status(500).json({ message: 'Internal server error.' });
    }
});

// Delete Order (DELETE)
app.delete('/order/:id', authenticateToken, async (req, res) => {
    const { id } = req.params;

    try {
        // Delete the order from the database
        const deleteQuery = 'DELETE FROM orders WHERE id = ? AND username = ?';
        db.promise().query(deleteQuery, [id, req.user.username])
            .then(([results]) => {
                if (results.affectedRows > 0) {
                    res.status(200).json({ message: 'Order deleted successfully.' });
                } else {
                    res.status(404).json({ message: 'Order not found or you are not authorized to delete this order.' });
                }
            })
            .catch(err => {
                console.error('Error deleting order:', err);
                res.status(500).json({ message: 'Internal server error.' });
            });

    } catch (err) {
        console.error('Error processing delete:', err);
        res.status(500).json({ message: 'Internal server error.' });
    }
});

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
