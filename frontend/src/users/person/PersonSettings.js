import React, {useEffect, useState} from "react";
import axios from "axios";
import ShelterAnimalList from "../../animal/ShelterAnimalList";
import {useParams} from "react-router-dom";
import {Link, useNavigate} from "react-router-dom";
import ShelterServerConstants from "../../util/ShelterServerConstants";
import PopupDeleteShelterByShelter from "../shelter/PopupDeleteShelterByShelter";
import PopupDeleteUserByUser from "./PopupDeleteUserByUser";

/* eslint-disable */

function PersonSettings() {
    axios.defaults.withCredentials = true;
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const {id} = useParams();
    const [user, setUser] = useState([]);
    const [error, setError] = useState(null);
    const [post, setPost] = useState({
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
                setPost(response.data); // update the post state
            })
            .catch((error) => {
                console.error("Error fetching user data:", error);
                setError(error);
            });
    }, [id]);

    const [modalOpen, setModalOpen] = useState(false);

    function handleSubmit(e) {
        e.preventDefault();
        axios
            .put(`http://localhost:8080/user/update`, JSON.stringify(post), {
                headers: {
                    'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                }
            })
            .then((response) => {
                setUser(response.data);
            })
            .catch((error) => {
                console.error("Error update user data:", error);
                setError(error);
            });
    }


    const handleInput = (e) => {
        setPost({...post, [e.target.name]: e.target.value});
        const {name, value} = e.target;
        setPost((prevState) => ({
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value,
            },
        }));
    };
    return (<div className="bg-background-pattern bg-opacity-20 max-w-none">
        <div className="px-10 font-display bg-white bg-opacity-90">
            <h2 className="text-center text-2xl text-orange font-bold p-10 h-fit">
                Twoje dane</h2>
            <div className='py-5 lg:flex justify-around md:block'>
                <div className="">
                    <h2 className=" text-md text-orange font-bold h-fit pb-5">
                        Aktualne dane</h2>
                    <label htmlFor="Firstname"
                           className=" tracking-wide text-brown text-s mb-2">
                        Imię: <p className="font-bold text-xl">{user.firstName}</p>
                    </label>
                    <label htmlFor="Lastname"
                           className=" tracking-wide text-brown text-s mb-2">
                        Nazwisko:<p className="font-bold text-xl"> {user.lastName}</p>
                    </label>
                    <label htmlFor="Email"
                           className=" tracking-wide text-brown text-s mb-2">
                        E-mail: <p className="font-bold text-xl">{user.email}</p>
                    </label>

                    <label htmlFor="City"
                           className=" tracking-wide text-brown text-s mb-2">
                        Miasto: <p className="font-bold text-xl">{user.address && user.address.city}</p>
                    </label>

                    <label htmlFor="Postal code"
                           className="tracking-wide text-brown text-s mb-2"
                           pattern="\d{2}-\d{3}">
                        Kod pocztowy:<p className="font-bold text-xl"
                                        pattern="\d{2}-\d{3}"
                    >
                        {user.address && user.address.postalCode && <p className="pt-2">Kod pocztowy: {user.address.postalCode.slice(0, 2)}{user.address.postalCode.slice(2)}{" "}</p>}</p>
                    </label>

                    <label htmlFor="Street"
                           className=" tracking-wide text-brown text-s mb-2">
                        Ulica: <p className="font-bold text-xl">{user.address && user.address.street}</p>
                    </label>
                    <label htmlFor="Building number"
                           className=" tracking-wide text-brown text-s mb-2">
                        Numer budynku: <p className="font-bold text-xl">{user.address && user.address.buildingNumber}</p>
                    </label>
                    <label htmlFor="Flat number"
                           className=" tracking-wide text-brown text-s mb-2">
                        Numer mieszkania:<p className="font-bold text-xl"> {user.address && user.address.flatNumber}</p>
                    </label>
                    <label htmlFor="Phone"
                           className=" tracking-wide text-brown text-s mb-2">
                        Telefon kontaktowy:<p className="font-bold text-xl">{user.address && user.address.phone} </p>
                    </label>
                    {modalOpen && <PopupDeleteUserByUser setOpenModal={setModalOpen} setLoading={setLoading} navigate={navigate} id={id} user={user}/>}
                    <button
                        type="button"
                        onClick={() => setModalOpen(true)}
                        className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown"
                    >
                        <p className="py-15 justify-center text-base text-center text-brown font-medium">
                            Usuń konto
                        </p></button>
                </div>
                {user ? (<div className="block px-30">
                    <h2 className=" text-md text-orange font-bold h-fit pb-5">
                        Tutaj możesz zaktualizować swoje dane.</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="firstName"
                                defaultValue={user.firstName}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj nazwisko."
                                name="lastName"
                                defaultValue={user.lastName}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="city"
                                defaultValue={user.address && user.address.city}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj miasto."
                                name="street"
                                defaultValue={user.address && user.address.street}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type="text"
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj kod pocztowy."
                                name="postalCode"
                                pattern="\d{2}-\d{3}"
                                defaultValue={user.address && user.address.postalCode && user.address.postalCode.slice(0, 2) +'-' + user.address.postalCode.slice(2)}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer budynku."
                                name="buildingNumber"
                                defaultValue={user.address && user.address.buildingNumber}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer mieszkania."
                                name="flatNumber"
                                defaultValue={user.address && user.address.flatNumber}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="">
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer telefonu kontaktowego."
                                name="phone"
                                defaultValue={user.address && user.address.phone}
                                onChange={handleInput}
                            />
                        </div>
                        <div className="m-auto text-center">
                            <button type="submit"
                                    className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                <p className="py-15 justify-center text-base text-center text-brown font-medium	">Aktualizuj
                                    dane</p>
                            </button>
                        </div>
                    </form>
                </div>) : (<div>
                    <p>Ładowanie danych...</p>
                </div>)}
            </div>
        </div>
    </div>);
}

export default PersonSettings;

