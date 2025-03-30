<?php 
$host = "localhost"; 						   $user = "root";  
$pass = "12345";  					        $dbname = "LOCKOUT1"; // Nombre real de tu base de datos

$conn = new mysqli($host, $user, $pass, $dbname);

if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}
?>
