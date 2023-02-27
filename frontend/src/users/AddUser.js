import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";


export default function AddUser() {

    let navigate = useNavigate();

    const [user, setUser] = useState({

        firstName:"",
        lastName:"",
        email:"",
        password:"",
        address: {
            street:"",
            city:"",
            postal_code:"",
            building_number:"",
            flat_number:"",
            phone:""
        }
    });
    const onInputChange=(e)=>{
        setUser({...user, [e.target.name]: e.target.value});
        const {name, value} = e.target;
        setUser((prevState) => ({
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value
            }
        }));
    };
    const onSubmit = async (e)=>{
        e.preventDefault();
        await axios.post("http://localhost:8080/registration/registerUser", user);
        navigate("/");
    };

    const {firstName, lastName, email, password, street, phone, city, building_number, flat_number, postal_code} = user

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
                    <h2 className="text-center m-4">Załóż konto</h2>
                    <form onSubmit={(e)=>onSubmit(e)}>
                        <div className="mb-3">
                            <label htmlFor="Firstname" className="form-label">
                                Imię:
                            </label>
                            <input
                                type={"text"}
                                className="form-control"
                                placeholder="Podaj imię."
                                name="firstName"
                                value={firstName}
                                onChange={(e)=>onInputChange(e)}
                                required
                            />
                            <div className="mb-3">
                                <label htmlFor="Lastname" className="form-label">
                                    Nazwisko:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj nazwisko."
                                    name="lastName"
                                    value={lastName}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Email" className="form-label">
                                    E-mail:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj adres e-mail."
                                    name="email"
                                    value={email}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Password" className="form-label">
                                    Hasło:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Wpisz hasło."
                                    name="password"
                                    value={password}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Street" className="form-label">
                                    Ulica:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj ulicę."
                                    name="street"
                                    value={street}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="City" className="form-label">
                                    Miasto:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj miasto."
                                    name="city"
                                    value={city}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Postal code" className="form-label">
                                    Kod pocztowy:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj kod pocztowy."
                                    name="postal_code"
                                    value={postal_code}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Building number" className="form-label">
                                    Numer budynku:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj numer budynku."
                                    name="building_number"
                                    value={building_number}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Flat number" className="form-label">
                                    Numer mieszkania:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj numer mieszkania."
                                    name="flat_number"
                                    value={flat_number}
                                    onChange={(e)=>onInputChange(e)}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Phone" className="form-label">
                                    Telefon kontaktowy:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj numer telefonu kontaktowego."
                                    name="phone"
                                    value={phone}
                                    onChange={(e)=>onInputChange(e)}
                                    required
                                />
                            </div>
                        </div>
                        <button type="submit" className="btn btn-outline-primary">
                            Zarejestruj
                        </button>
                        <Link type="submit" className="btn btn-danger mx-2" to="/">
                            Anuluj
                        </Link>
                    </form>
                </div>

            </div>
        </div>
    )
}