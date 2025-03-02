<?php
// Cabecera para indicar que la respuesta es JSON
header("Content-Type: application/json");

// Configuración de la base de datos
$servername = "192.168.1.35";
$username = "remote_user"; // Usuario por defecto de XAMPP
$password = "your_password"; // Contraseña por defecto de XAMPP (vacía)
$dbname = "lockout"; // Nombre de tu base de datos

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar conexión
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Obtener el número de la solicitud GET
$numero = $_GET['numero'];

// Buscar el número en la base de datos
$sql = "SELECT * FROM numsospechoso WHERE numero = '$numero'";
$result = $conn->query($sql);

// Preparar la respuesta
if ($result->num_rows > 0) {
    echo json_encode(["status" => "found"]);
} else {
    echo json_encode(["status" => "not_found"]);
}

// Cerrar la conexión
$conn->close();
?>