import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";

export default function AddShelter() {

    let navigate = useNavigate();

    const [shelter, setShelter] = useState({
        email:"",
        shelterName:"",
        password:"",
        userType:"SHELTER",
        address: {
            street:"",
            city:"",
            postalCode:"",
            buildingNumber:"",
            flatNumber:"",
            phone:"",
            krsNumber:""
        }
    });
    const onInputChange=(e)=>{
        setShelter({...shelter, [e.target.name]: e.target.value});
        const {name, value} = e.target;
        setShelter((prevState) => ({
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value
            }
        }));
    };
    const onSubmit = async (e)=>{
        e.preventDefault();
        await axios.post("http://localhost:8080/registration/register", shelter);
        navigate("/");
    };

    const {email, password, shelterName, street, city, postalCode, buildingNumber, flatNumber, phone, krsNumber} = shelter

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
                    <h2 className="text-center m-4">Zarejestruj schronisko</h2>
                    <form onSubmit={(e)=>onSubmit(e)}>
                        <div className="mb-3">
                            <label htmlFor="Shelter name" className="form-label">
                                Nazwa schroniska:
                            </label>
                            <input
                                type={"text"}
                                className="form-control"
                                placeholder="Podaj nazwę schroniska."
                                name="shelterName"
                                value={shelterName}
                                onChange={(e)=>onInputChange(e)}
                                required
                            />
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
                                    name="postalCode"
                                    value={postalCode}
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
                                    name="buildingNumber"
                                    value={buildingNumber}
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
                                    name="flatNumber"
                                    value={flatNumber}
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
                            <div className="mb-3">
                                <label htmlFor="KRS_number" className="form-label">
                                    Numer KRS:
                                </label>
                                <input
                                    type={"text"}
                                    className="form-control"
                                    placeholder="Podaj numer KRS."
                                    name="krsNumber"
                                    value={krsNumber}
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