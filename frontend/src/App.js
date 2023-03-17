import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import Navbar from "./layout/Navbar";
import Home from "./layout/Home";
import AddUser from "./users/AddUser";
import AddShelter from "./users/AddShelter";
import Hero from "./layout/Hero";
import Login from "./users/Login/Login";
import './index.css';
import LoggedInUser from "./users/Login/LoggedInUser";
import Footer from "./layout/Footer";
function App() {
    const token = localStorage.getItem("token")
  return (
    <div className="m-auto font-display">
      <Router>
        <Navbar/>
          <Hero/>
        <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/adduser" element={<AddUser />} />
            <Route exact path="/addshelter" element={<AddShelter />} />
            <Route exact path="/loggedinuser" element={<LoggedInUser />} />
            <Route exact path="/login" element= {token ? <Navigate to="/loggedinuser" /> : <Login />} />
        </Routes>
      </Router>
        <Footer/>

    </div>
  );
}

export default App;
