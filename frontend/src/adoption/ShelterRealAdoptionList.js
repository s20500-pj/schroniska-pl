import axios from "axios";
import React, {useState, useEffect} from "react";
import Table from "../util/Table";
import {Link} from "react-router-dom";
import {ADOPTION_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";

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
                Cell: ({value}) => ADOPTION_STATUS_OPTIONS[value],
            },
            {
                Header: "Ważna do:",
                accessor: "validUntil",
            },
            {
                Header: "Szczegóły zwierzaka",
                accessor: "animal.id",
                Cell: ({value}) => (
                    <Link to={`/animalDetails/${value}`}>
                        <button type="submit"
                                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                            <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Zobacz zwierzę</p>
                        </button>                     </Link>
                ),
            },
            {
                Header: "Szczegóły adopcji",
                accessor: "id",
                Cell: ({value}) => (
                    <Link to={`/adoptionDetails/${value}`}>
                        <button type="submit"
                                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                            <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Zobacz szczegóły adopcji</p>
                        </button>
                    </Link>
                ),
            }
        ],
    },
];

function ShelterRealAdoptionList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.get("http://localhost:8080/adoption/getAll/REAL");
                setData(result.data);
            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);

    return (
        <div className="App">
            <Table columns={columns} data={data} pageSize={20}/>
        </div>
    );
}

export default ShelterRealAdoptionList;
