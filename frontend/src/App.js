import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Navbar from "./layout/Navbars/Navbar";
import Home from "./layout/Home";
import AddUser from "./users/unlogged/AddUser";
import AddShelter from "./users/unlogged/AddShelter";
import Hero from "./layout/Hero";
import Login from "./users/Login/Login";
import './index.css';
import LoggedInUser from "./users/Login/LoggedInUser";
import Footer from "./layout/Footer";
import {useState} from "react";
import NavbarShelter from "./layout/Navbars/NavbarShelter";
import AnimalList from "./animal/AnimalList";
import AddAnimal from "./animal/AddAnimal";
import axios from "axios";
import ShelterPage from "./users/shelter/ShelterPage";
import AdminPage from "./users/admin/AdminPage";
import PersonPage from "./users/person/PersonPage";
import NavbarAdmin from "./layout/Navbars/NavbarAdmin";
import NavbarPerson from "./layout/Navbars/NavbarPerson";

function App() {
    axios.defaults.withCredentials = true
    const [storageFilled, setStorageFilled] = useState(false)
    const loggingInfo = (info) => {
        setStorageFilled(info);
    }
    const userType = localStorage.getItem("userType");

    return (
        <div className="m-auto font-display">
            <Router>

                {(userType === "ADMIN") ? <NavbarAdmin/> :
                    (userType === "SHELTER") ? <NavbarShelter/> :
                        (userType === "PERSON") ? <NavbarPerson/> : <Navbar/>}

                <Routes>
                    <Route exact path="/" element={(userType === "PERSON") ? <PersonPage/> :
                        (userType === "SHELTER") ? <ShelterPage/> : (userType === "ADMIN") ? <AdminPage/> :
                            <Hero/>}/>
                    <Route exact path="/adduser" element={<AddUser/>}/>
                    <Route exact path="/addshelter" element={<AddShelter/>}/>
                    <Route exact path="/loggedinuser" element={<LoggedInUser/>}/>
                    <Route exact path="/login" element={<Login loggingInfo={loggingInfo}/>}/>
                    <Route exact path="/animalList" element={<AnimalList/>}/>
                    {localStorage.getItem("userType") === "SHELTER" ? <Route exact path="/addAnimal" element={<AddAnimal/>}/> : <Route exact path="/" element={<Home/>}/>}
                </Routes>
            </Router>
            <Footer/>
        </div>
    );
}

export default App;
