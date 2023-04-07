import React from "react";
import {Link, Route, Routes, useNavigate} from "react-router-dom";
import axios from "axios";
import {Sidebar, Menu, MenuItem, SubMenu} from 'react-pro-sidebar';
import UserSettings from "./Pages/UserSettings";
import LogoutRoundedIcon from "@mui/icons-material/LogoutRounded";
import AllDogs from "./Pages/AllDogs";

function UserCare() {
    return null;
}

function UserNavigationBar(){
    axios.defaults.withCredentials = true
    const navigate = useNavigate();
    const firstName = localStorage.getItem("firstName");
    const shelterName = localStorage.getItem("shelterName");
    const handleLogout = async () => {
        await axios.get("http://localhost:8080/auth/logout");
        localStorage.clear();
        navigate('/');
    };

        return (
            <div style={{ display: "flex", height: "100vh" }}>
                <Sidebar className="app">
                    <Menu>
                        <SubMenu label="Zwierzęta" component={<Link to="/useranimals" className="link"/>}>
                            <MenuItem> Adopcja </MenuItem>
                            <MenuItem> Adopcja wirtualna </MenuItem>
                        </SubMenu>
                        <MenuItem
                            component={<Link to="/usercare" className="link" />}
                        >
                            Opieka
                        </MenuItem>
                        <MenuItem
                            component={<Link to="/usersettings" className="link" />}
                        >
                            Ustawienia
                        </MenuItem>
                        <MenuItem icon={<LogoutRoundedIcon />}
                       onClick={handleLogout}> Wyloguj się </MenuItem>
                    </Menu>
                </Sidebar>
                <section>
                    <Routes>
                        <Route path="UserSettings" element={<UserSettings />} />
                        <Route path="UserAnimals" element={<AllDogs />} />
                        <Route path="AllDogs" element={<AllDogs />} />
                        <Route path="UserCare" element={<UserCare />} />
                    </Routes>
                </section>
            </div>
        );
};
export default UserNavigationBar;