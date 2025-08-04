import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const { isAuthenticated, logout } = useAuth();

  return (
    <nav style={{ padding: "1rem", backgroundColor: "#eee" }}>
      <Link to="/">Home</Link> | <Link to="/dashboard">Dashboard</Link> |{' '}
      {!isAuthenticated ? (
        <>
          <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
        </>
      ) : (
        <button onClick={logout}>Logout</button>
      )}
    </nav>
  );
};

export default Navbar;