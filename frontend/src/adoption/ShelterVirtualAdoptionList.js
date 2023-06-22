import axios from "axios";
import React, {useEffect, useState} from "react";
import Table from "../util/Table";
import {Link} from "react-router-dom";
import ShelterServerConstants from "../util/ShelterServerConstants";
import {ADOPTION_VIRTUAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import {formatDate} from "../util/DateUtils";

const columns = [
    {
        Header: "Adopcje realne schroniska",
        columns: [
            {
                Header: "Zdjęcie",
                accessor: "animal.imagePath",
                Cell: ({value}) => <img src={value} alt=""/>
            },
            {
                Header: "Imię",
                accessor: "animal.name"
            },
            {
                Header: "Gatunek",
                accessor: "animal.species",
                Cell: ({value}) => SPECIES_OPTIONS[value]
            },
            {
                Header: "Płeć",
                accessor: "animal.sex",
                Cell: ({value}) => SEX_OPTIONS[value]
            },
            {
                Header: "Status adopcji",
                accessor: "adoptionStatus",
                Cell: ({value}) => ADOPTION_VIRTUAL_STATUS_OPTIONS[value],
            },
            {
                Header: "Ważna do:",
                accessor: "validUntil",
                Cell: ({value}) => formatDate(value)
            },
            {
                Header: "Imię adoptującego:",
                accessor: "user.firstName",
            },
            {
                Header: "Email adoptującego:",
                accessor: "user.email",
            },
            {
                Header: "Szczegóły zwierzaka",
                accessor: "animal.id",
                Cell: ({value}) => (
                    <Link to={`/animalDetails/${value}`}>
                        <button type="submit"
                                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                            <p className="py-15 justify-center text-base text-center text-brown font-medium	">Zobacz
                                zwierzę</p>
                        </button>
                    </Link>
                ),
            },
        ],
    },
];

function ShelterRealAdoptionList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);
    const [adoption, setAdoption] = useState({
        adoptionStatus: "",
        adoptionType: "VIRTUAL",
    });

    const adoptionMap = new Map(Object.entries(adoption)
        .filter(([key, value]) => value !== ""));

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/adoption/getAdoptions",
                    JSON.stringify(Object.fromEntries(adoptionMap)),
                    {
                        headers: {
                            'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                        }
                    });
                setData(result.data);
                console.log(result.data)
            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);

    const onInputChange = (e) => {
        setAdoption({...adoption, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const result = await axios.post(
                ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/adoption/getAdoptions",
                JSON.stringify(Object.fromEntries(adoptionMap)), {
                    withCredentials: true,
                    headers: {
                        'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                    }
                }
            );
            setData(result.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleClear = () => {
        setAdoption({...adoption, adoptionStatus: ""});
    };

    return (
        <div className="md:flex p-5 h-fit sm:block sm:h-fit">
            <div className="bg-background-pattern bg-opacity-20 max-w-none md:w-1/4 sm:w-fit sm:h-fit">
                <div className="font-display bg-white bg-opacity-90">
                    <h2 className="text-center text-2xl text-orange font-bold p-5">Filtry</h2>
                    <form onSubmit={(e) => handleSubmit(e)} className="w-full m-auto">
                        <div className="flex flex-wrap">
                            <div className=" w-full px-3 ">
                                <label htmlFor="adoptionStatus"
                                       className="block uppercase tracking-wide text-brown text-sm font-bold py-2">
                                    Status adopcji wirtualnej:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border-2 border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="adoptionStatus"
                                    value={adoption.adoptionStatus}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    {Object.entries(ADOPTION_VIRTUAL_STATUS_OPTIONS).map(([key, value]) => (
                                        <option key={key} value={key}>
                                            {value}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="w-full">
                                <div className="flex justify-around">
                                    <button type="submit"
                                            className=" w-full m-2 flex px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="m-auto text-brown font-medium">Filtruj</p>
                                    </button>
                                    <button onClick={handleClear}
                                            className="w-full m-2 flex px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="m-auto text-brown font-medium">Czyść
                                            filtry</p>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <Table columns={columns} data={data} pageSize={20}/>
        </div>
    );
}

export default ShelterRealAdoptionList;

