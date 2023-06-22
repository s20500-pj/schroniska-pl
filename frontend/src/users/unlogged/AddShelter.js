import React, {useState} from "react";
import axios from "axios";
import SuccessPopup from "./SuccessPopup";
import {Link, useNavigate} from "react-router-dom";
import Modal from "./Modal";
import PopupErrorRegistration from "./PopupErrorRegistration";
import pdf from "../PolitykaPrywatnosci.pdf";

export default function AddShelter() {
    const [error, setError] = useState("");
    const [modalOpen, setModalOpen] = useState(false);
    let navigate = useNavigate();
    const [shelter, setShelter] = useState({
        email: "",
        shelterName: "",
        password: "",
        userType: "SHELTER",
        iban: "",
        address: {
            street: "",
            city: "",
            postalCode: "",
            buildingNumber: "",
            flatNumber: "",
            phone: "",
            krsNumber: ""
        }
    });
    const [errorOpen, setErrorOpen] = useState(false);
    const handleCancelClick = () => {
        navigate("/");
    };
    const onInputChange = (e) => {
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
    const onSubmit = async (e) => {
        try {
            e.preventDefault();
            await axios.post("http://localhost:8080/registration/register", shelter);
            setModalOpen(true);
        } catch (error) {
            console.log(error.response.data)
            setError(error.response.data || "Something went wrong");
            setError(error.response.data);
            setErrorOpen(true);
        }
    };

    const {
        email,
        password,
        shelterName,
        iban,
        street,
        city,
        postalCode,
        buildingNumber,
        flatNumber,
        phone,
        krsNumber
    } = shelter

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10">Zarejestruj schronisko</h2>
                {modalOpen && <Modal setOpenModal={setModalOpen}/>}
                {errorOpen && <PopupErrorRegistration setOpenError={setErrorOpen} error={error}/>}
                <form onSubmit={(e) => onSubmit(e)} className="w-full max-w-lg m-auto py-10">
                    <div className="flex flex-wrap">
                        <div className="md:w-1/2 px-3 mb-6 md:mb-0">
                            <label htmlFor="Shelter name"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Nazwa schroniska:
                            </label>
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj nazwę schroniska."
                                name="shelterName"
                                value={shelterName}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="Email"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                E-mail:
                            </label>
                            <input
                                type={"email"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj adres e-mail."
                                name="email"
                                value={email}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="Password"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Hasło:
                            </label>
                            <input
                                type={"password"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Wpisz hasło."
                                name="password"
                                value={password}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="IBAN"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Numer konta bankowego (IBAN):
                            </label>
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Wpisz numer konta."
                                name="iban"
                                value={iban}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="Street"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Ulica:
                            </label>
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj ulicę."
                                name="street"
                                value={street}
                                onChange={(e) => onInputChange(e)}
                                required
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
                                value={city}
                                onChange={(e) => onInputChange(e)}
                                required
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
                                value={postalCode}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="Building number"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Numer budynku:
                            </label>
                            <input
                                type={"number"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer budynku."
                                name="buildingNumber"
                                value={buildingNumber}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="Flat number"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Numer mieszkania:
                            </label>
                            <input
                                type={"number"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer mieszkania."
                                name="flatNumber"
                                value={flatNumber}
                                onChange={(e) => onInputChange(e)}
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
                                value={phone}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="KRS_number"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                Numer KRS:
                            </label>
                            <input
                                type={"number"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj numer KRS."
                                name="krsNumber"
                                value={krsNumber}
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="py-3 px-4">
                            <input id="default-radio-1" type="radio" value="" name="default-radio" required
                                   className="w-4 h-4 text-orange bg-orange border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"/>
                            <label htmlFor="default-radio-1"
                                   className="ml-2 text-sm font-medium text-orange ">
                                Oświadczam, że zapoznałem się z {' '}
                                <a href = {pdf} target = "_blank" className="underline">Polityką prywatności </a>
                                i akceptuje jej zasady.
                            </label>
                        </div>
                    </div>
                    <button type="submit"
                            className="px-10 py-2 m-5 border-2 border-orange rounded bg-white  hover:bg-orange text-white active:bg-brown ">
                        <p className="py-15 justify-center text-base text-center text-brown font-medium	">Zarejestruj</p>
                    </button>
                    <button onClick={handleCancelClick} type="submit"
                            className="px-10 py-2 m-5 border-2 border-orange rounded bg-white  hover:bg-orange text-white active:bg-brown ">
                        <a
                            href="/"
                            className='py-15 justify-center text-base text-center text-brown font-medium	'
                        >
                            Anuluj </a>
                    </button>
                </form>
            </div>
        </div>
    )
}

