import axios from "axios";
import React, {useEffect, useState} from "react";
import Table from "../util/Table";
import {Link} from "react-router-dom";
import ShelterServerConstants from "../util/ShelterServerConstants";
import {ADOPTION_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import {formatDate} from "../util/DateUtils";

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
                Cell: ({value}) => formatDate(value)
            },
            {
                Header: "Szczegóły",
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
                const result = await axios.post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/adoption/getAdoptions",
                    JSON.stringify(Object.fromEntries(new Map(Object.entries({adoptionType: "REAL"})))),
                    {
                        headers: {
                            'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                        }
                    });
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
