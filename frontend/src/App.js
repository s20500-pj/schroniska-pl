import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Navbar from "./layout/Navbar";
import Home from "./layout/Home";
import AddUser from "./users/AddUser";
import AddShelter from "./users/AddShelter";
import Login from "./users/Login";

function App() {
  return (
    <div className="App">
      <Router>
        <Navbar />
        <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/adduser" element={<AddUser />} />
            <Route exact path="/addshelter" element={<AddShelter />} />
            <Route exact path="/login" element={<Login />} />
        </Routes>
      </Router>

    </div>
  );
}

export default App;
