import { useState } from "react";
import { register } from "../api/auth";

const Register = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await register({ username, email, password });
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <form onSubmit={handleRegister}>
      <h2>Register</h2>
      {success && <p style={{ color: "green" }}>Registration successful!</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required /><br />
      <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required /><br />
      <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required /><br />
      <button type="submit">Register</button>
    </form>
  );
};

export default Register;