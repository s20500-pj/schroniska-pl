import {Link} from "react-router-dom";

export default function Navbar() {
    return (
        <div>
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container-fluid">
                    <img src='logo.svg'/>
                    <Link className="btn btn-outline-danger" to="/">Home</Link>
                    <Link className="btn btn-outline-light" to="/adduser">Zarejestruj się</Link>
                    <Link className="btn btn-outline-light" to="/addshelter">Zarejestruj schronisko</Link>
                    <Link className="btn btn-outline-light" to="/login">Zaloguj się</Link>
                </div>
            </nav>
        </div>
    )
}