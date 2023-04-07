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
import UserSettings from "./users/Panels/User/Pages/UserSettings";
import React from "react";
import UserNavigationBar from "./users/Panels/User/UserNavigationBar";
import AllDogs from "./users/Panels/User/Pages/AllDogs";

function UserAnimals() {
    return null;
}

function UserCare() {
    return null;
}

function App() {
    const token = localStorage.getItem("authToken")
    const userType = localStorage.getItem("userType")
  return (
    <div className="m-auto font-display">
        <Navbar/>
        {localStorage.length == 0 ?  <Hero/> : <UserNavigationBar/> }
        <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/adduser" element={<AddUser />} />
            <Route exact path="/addshelter" element={<AddShelter />} />
            <Route exact path="/loggedinuser" element={<LoggedInUser />} />
            <Route exact path="/login" element= {token ? <Navigate to="/loggedinuser" /> : <Login />} />
            <Route exact path="/usersettings" element={<UserSettings />} />
            <Route exact path="/alldogs" element={<AllDogs />} />
            <Route exact path="/useranimals" element={<UserAnimals />} />
            <Route exact path="/usercare" element={<UserCare />} />
        </Routes>
        <Footer/>
    </div>
  );
}

export default App;
