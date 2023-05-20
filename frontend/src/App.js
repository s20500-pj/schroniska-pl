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
import ShelterAnimalList from "./animal/ShelterAnimalList";
import UserVirtualAdoptionList from "./adoption/UserVirtualAdoptionList";
import UserRealAdoptionList from "./adoption/UserRealAdoptionList";
import PersonSettings from "./users/person/PersonSettings";
import AnimalDetails from "./animal/AnimalDetails";
import ShelterRealAdoptionList from "./adoption/ShelterRealAdoptionList";
import ShelterVirtualAdoptionList from "./adoption/ShelterVirtualAdoptionList";
import AdoptionDetails from "./adoption/AdoptionDetails";
import ShelterList from "./lists/ShelterlList";
import UsersList from "./lists/UsersList";
import ShelterDetails from "./users/shelter/ShelterDetails";
import MailConfirmationPage from "./origin/MailConfirmationPage";
import PayUConfirmationPage from "./origin/PayUConfirmationPage";
import UserActivityList from "./activity/UserActivityList";
import ShelterActivityList from "./activity/ShelterActivityList";
import Contact from "./users/unlogged/Contact";
// import ShelterAnimalListUnlogged from "./animal/ShelterAnimalListUnlogged";

function App() {
    axios.defaults.withCredentials = true
    const [storageFilled, setStorageFilled] = useState(false)
    const loggingInfo = (info) => {
        setStorageFilled(info);
    }
    const userType = localStorage.getItem("userType");

    return (
        <div className="m-auto font-display h-fit">
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
                    <Route exact path="/contact" element={<Contact/>}/>
                    <Route exact path="/loggedinuser" element={<LoggedInUser/>}/>
                    <Route exact path="/login" element={<Login loggingInfo={loggingInfo}/>}/>
                    <Route exact path="/animalList" element={<AnimalList/>}/>
                    <Route exact path="/animalDetails/:id" element={<AnimalDetails/>}/>
                    <Route exact path="/shelterList" element={<ShelterList/>}/>
                    <Route exact path="/shelterDetails/:id" element={<ShelterDetails/>}/>
                    <Route exact path="/userList" element={<UsersList/>}/>
                    <Route exact path="/shelterAnimalList" element={<ShelterAnimalList/>}/>
                    {/*<Route exact path="/shelterAnimalListUnlogged" element={<ShelterAnimalListUnlogged/>}/>*/}
                    {userType === "SHELTER" ? <Route exact path="/addAnimal" element={<AddAnimal/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "SHELTER" ? <Route exact path="/shelterAnimalList" element={<ShelterAnimalList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "PERSON" ?
                        <Route exact path="/userVirtualAdoptionList" element={<UserVirtualAdoptionList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "PERSON" ?
                        <Route exact path="/userRealAdoptionList" element={<UserRealAdoptionList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "SHELTER" ?
                        <Route exact path="/shelterRealAdoptionList" element={<ShelterRealAdoptionList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "SHELTER" ?
                        <Route exact path="/shelterVirtualAdoptionList" element={<ShelterVirtualAdoptionList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "PERSON" ?
                        <Route exact path="/userActivityList" element={<UserActivityList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "SHELTER" ?
                        <Route exact path="/shelterActivityList" element={<ShelterActivityList/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "PERSON" ? <Route exact path="/personsettings" element={<PersonSettings/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "SHELTER" ? <Route exact path="/adoptionDetails/:id" element={<AdoptionDetails/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType == null ?
                        <Route exact path="/confirmation" element={<MailConfirmationPage/>}/> :
                        <Route exact path="/" element={<Home/>}/>}
                    {userType === "PERSON" ? <Route exact path="/payment" element={<PayUConfirmationPage/>}/> :
                        <Route exact path="/" element={<Home/>}/>}

                </Routes>
            </Router>
            <Footer/>
        </div>
    );
}

export default App;
