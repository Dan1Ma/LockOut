<?php
// Cabecera para indicar que la respuesta es JSON
header("Content-Type: application/json");

// Configuración de la base de datos
$servername = "192.168.1.35";
$username = "remote_user";
$password = "your_password";
$dbname = "lockout";

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar conexión
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]));
}

// Obtener el número de la solicitud GET
$numero = $_GET['numero'] ?? '';

// Prevenir inyecciones SQL (buena práctica)
$numero = $conn->real_escape_string($numero);

// Buscar el número en la base de datos
$sql = "SELECT numero, prefijo FROM numsospechoso WHERE numero = '$numero'";
$result = $conn->query($sql);

// Preparar la respuesta
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    echo json_encode([
        "status" => "found",
        "numero" => $row['numero'],
        "prefijo" => $row['prefijo']
    ]);
} else {
    echo json_encode(["status" => "not_found"]);
}

// Cerrar la conexión
$conn->close();
?>
