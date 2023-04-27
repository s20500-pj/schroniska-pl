import React, {useEffect, useState} from "react";
import axios from "axios";
import ShelterAnimalList from "../../animal/ShelterAnimalList";
import {useParams} from "react-router-dom";
import {Link, useNavigate} from "react-router-dom";
import ShelterServerConstants from "../../util/ShelterServerConstants";
/* eslint-disable */

function PersonSettings() {
    axios.defaults.withCredentials = true;
    const {id} = useParams();
    const [user, setUser] = useState({});
    const [error, setError] = useState(null);
    const [post, setPost] = useState({
        id:id,
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

    useEffect(() => {
        axios.get(`http://localhost:8080/user`)
            .then((response) => {
                console.log(response.data);
                setUser(response.data);
                setPost(response.data);
            })
            .catch((error) => {
                console.error("Error fetching user data:", error);
                setError(error);
            });
    }, [id]);


    const deleteUser = async (id) => {
        axios.defaults.withCredentials = true;
        console.log(id);
        await axios.delete(`http://localhost:8080/user/delete/${id}`);

    }

    function handleSubmit(e) {
        e.preventDefault();
        axios
            .put(`http://localhost:8080/user/update`, JSON.stringify(post),
                {
                    headers: {
                        'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                    }
                })
            .then((response) => {
                console.log(post);
                console.log(response.data);
                setUser(response.data);
            })
            .catch((error) => {
                console.error("Error update user data:", error);
                setError(error);
            });
    }
    const handleInput = (event) => {
        console.log(event);
        setPost({...post, [event.target.name]: event.target.value});
    };
    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10 h-fit">
                    Twoje dane</h2>
                <h2 className="text-center text-md text-brown font-bold h-fit">
                    Tutaj możesz zaktualizować swoje dane.</h2>
                <div className='py-5 lg:flex justify-around md:block'>
                    {user ? (
                        <div className="block px-30">
                            <form onSubmit={handleSubmit}>
                                <div className="md:w-1/2 px-3 mb-6 md:mb-0">
                                    <label htmlFor="Firstname"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Imię: {user.firstName}
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="dupa"
                                        name="firstName"
                                        defaultValue={user.firstName}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Lastname"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Nazwisko: {user.lastName}

                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj nazwisko."
                                        name="lastName"
                                        // value={post.lastName}
                                        defaultValue={user.lastName}
                                        onChange={(e) => handleInput(e)}

                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Email"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        E-mail:
                                    </label>
                                    <input
                                        type={"email"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj adres e-mail."
                                        name="email"
                                        // value={post.email}
                                        onChange={handleInput}
                                    />
                                </div>

                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Street"
                                           className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Ulica:
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj ulicę."
                                        name="street"
                                        //value={post.street}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="City"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Miasto:
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj miasto."
                                        name="city"
                                        ///value={post.city}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Postal code"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Kod pocztowy:
                                    </label>
                                    <input
                                        type="text"
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj kod pocztowy."
                                        name="postalCode"
                                        pattern="\d{2}-\d{3}"
                                        // value={post.postalCode}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Building number"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Numer budynku:
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj numer budynku."
                                        name="buildingNumber"
                                        //value={post.buildingNumber}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Flat number"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Numer mieszkania:
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj numer mieszkania."
                                        name="flat_number"
                                        //value={post.flatNumber}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="w-full md:w-1/2 px-3">
                                    <label htmlFor="Phone"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Telefon kontaktowy:
                                    </label>
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Podaj numer telefonu kontaktowego."
                                        name="phone"
                                        //value={post.phone}
                                        onChange={handleInput}

                                    />
                                </div>
                                <div className="m-auto text-center">
                                    <button type="submit"
                                        //onClick={handleSubmit}
                                            className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Aktualizuj
                                            dane</p>
                                    </button>
                                </div>
                            </form>
                        </div>
                    ) : (
                        <div>
                            <p>Ładowanie danych...</p>
                        </div>
                    )}
                </div>

                <button
                    type="button"
                    onClick={deleteUser}
                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown"
                >
                    <p className="py-15 justify-center text-base text-center text-brown font-medium">
                        Usuń użytkownika
                    </p></button>
            </div>

        </div>
    );
}

export default PersonSettings;


