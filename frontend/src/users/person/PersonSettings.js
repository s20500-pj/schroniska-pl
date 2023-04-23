import React, {useEffect, useState} from "react";
import axios from "axios";
import ShelterAnimalList from "../../animal/ShelterAnimalList";
import {useParams} from "react-router-dom";

function PersonSettings() {
    const {id} = useParams();
    axios.defaults.withCredentials = true;
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        userType: "PERSON",
        address: {
            street: "",
            city: "",
            postalCode: "",
            buildingNumber: "",
            flatNumber: "",
            phone: "",
        },
    });

    const [editID, setEditID] = useState()
    const [refresh, setRefresh] = useState(0)

    const { firstName } = formData;
    const Id = localStorage.getItem('userId')
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };
    // const handleUpdate = () => {
    //     if (firstName) {
    //         axios.put(`http://localhost:8080/user/update`)
    //             .then(res => {
    //                 setFormData({firstName: "" });
    //                 setRefresh(refresh + 1)
    //             })
    //             .catch(err => console.log(err))
    //
    //     }
    // };

    useEffect(() => {
        axios.get(`http://localhost:8080/user/${Id}`)
            .then((response) => {
                console.log(response.data);
                setUser(response.data);
            })
            .catch((error) => {
                console.error("Error fetching user data:", error);
                setError(error);
            });
    }, [id]);

    if (error) {
        return <div>Błąd: {error.message}. Brak dostępu</div>;
    }


    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10 h-fit">
                    Twoje dane</h2>
                <h2 className="text-center text-md text-brown font-bold h-fit">
                    Tutaj możesz zaktualizować swoje dane.</h2>
                <div className='py-5 lg:flex justify-around md:block'>
                    { user ? (
                        <div>
                            <div className="">
                                <label htmlFor="Firstname"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Imię: {user.firstName}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień imię"
                                    name="firstName"
                                    value={firstName}
                                    required
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="">
                                <label htmlFor="Lastname"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Nazwisko:
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Podaj nazwisko."
                                    name="lastName"
                                    // value={lastName}
                                    required
                                />
                            </div>
                            <div className="">
                                <label htmlFor="lastName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Telefon: {user.address.phone}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień nazwisko."
                                    name="lastName"
                                    value={user.address.phone}
                                    required
                                />
                            </div>
                            <div className="">
                                <label htmlFor="lastName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Miasto: {user.address.city}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień nazwisko."
                                    name="lastName"
                                    // value={lastName}
                                    required
                                />
                            </div>
                            <div className="">
                                <label htmlFor="lastName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Kod pocztowy: {user.address.postalCode}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień nazwisko."
                                    name="lastName"
                                    // value={lastName}
                                    required
                                />
                            </div>
                            <div className="">
                                <label htmlFor="lastName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Ulica: {user.address.street}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień nazwisko."
                                    name="lastName"
                                    // value={lastName}
                                    required
                                />
                            </div>
                            <div className="">
                                <label htmlFor="lastName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                    Numer mieszkania: {user.address.flatNumber}
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Zmień nazwisko."
                                    name="lastName"
                                    // value={lastName}
                                    required
                                />
                                <div className="">
                                    <label htmlFor="lastName"
                                           className="block uppercase tracking-wide text-brown text-md font-bold mb-2">
                                        Numer domu: {user.address.buildingNumber}
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-2 mb-1 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Zmień nazwisko."
                                        name="lastName"
                                        // value={lastName}
                                        required
                                    />
                                </div>
                            </div>

                        </div>
                    ) : (
                        <div>
                            <p>Ładowanie danych...</p>
                        </div>
                    )}
                </div><div className="m-auto text-center">
                <button type="submit" onClick={() => {
                    setEditID(user.id)}}
                        className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Aktualizuj dane</p>
                </button></div>
            </div>
        </div>
    );
}

export default PersonSettings;

