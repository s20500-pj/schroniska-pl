import axios from "axios";
import React, {useState, useEffect, useMemo} from "react";
import Table from "../util/Table";
import {Link} from "react-router-dom";

const ADOPTION_STATUS_OPTIONS = {
    REQUEST_REVIEW: "Zgłoszenie jest rozpatrywane",
    REQUIRES_MANUAL_INVITATION: "Oczekuje na kontakt ze schroniska",
    PENDING: "Adopcja w toku",
    SHELTER_INVITED: "Zaproszenie do schroniska",
    PENDING_SHELTER_INVITED: "Wizyta w toku",
    VISITED: "Schronisko odwiedzone",
    DECLINED: "Odmowa",
    ADOPTED: "Zwierzę zaadoptowane",
    VIRTUAL_ADOPTED: "Wirtualna adopcja",
};

const SPECIES_OPTIONS = {
    CAT: "Kot",
    DOG: "Pies"
};

const SEX_OPTIONS = {
    MALE: "Samiec",
    FEMALE: "Samica",
    UNKNOWN: "Nieznany"
};

const columns = [
    {
        Header: "Twoje adopcje realne",
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
                Header: "Nazwa schroniska",
                accessor: "animal.shelter.shelterName"
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
                Header: "Szczegóły",
                accessor: "animal.id",
                Cell: ({value}) => (
                    <Link to={`/animalDetails/${value}`}>
                        Zobacz szczegóły
                    </Link>
                ),
            }
        ],
    },
];

function UserRealAdoptionList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.get("http://localhost:8080/adoption/getUserAdoptions/REAL");
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

export default UserRealAdoptionList;
