import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Navbar from "./layout/Navbar";
import Home from "./layout/Home";
import AddUser from "./users/AddUser";
import AddShelter from "./users/AddShelter";
import Hero from "./layout/Hero";
import Login from "./users/Login/Login";
import './index.css';
import LoggedInUser from "./users/Login/LoggedInUser";
import Footer from "./layout/Footer";
import {useState} from "react";
import NavbarLoggedIn from "./layout/NavbarLoggedIn";
import AnimalList from "./animal/AnimalList";
import AddAnimal from "./animal/AddAnimal";
import axios from "axios";

function App() {
    axios.defaults.withCredentials = true
    const [storageFilled, setStorageFilled] = useState(false)
    const loggingInfo = (info) => {
        setStorageFilled(info)
    }

    return (
        <div className="m-auto font-display">
            <Router>

                {localStorage.getItem("userLoggedIn") ? <NavbarLoggedIn/> : <Navbar/>}
                <Hero/>
                <Routes>
                    <Route exact path="/" element={<Home/>}/>
                    <Route exact path="/adduser" element={<AddUser/>}/>
                    <Route exact path="/addshelter" element={<AddShelter/>}/>
                    <Route exact path="/loggedinuser" element={<LoggedInUser/>}/>
                    <Route exact path="/login" element={<Login loggingInfo={loggingInfo}/>}/>
                    <Route exact path="/animalList" element={<AnimalList/>}/>
                    <Route exact path="/addAnimal" element={<AddAnimal/>}/>
                </Routes>
            </Router>
            <Footer/>
        </div>
    );
}

export default App;
