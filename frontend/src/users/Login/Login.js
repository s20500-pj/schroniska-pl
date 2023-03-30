import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function Login() {
    axios.defaults.withCredentials = true
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setError("");

        try {
            const response = await axios.post("http://localhost:8080/auth/authenticate", {
                email,
                password,
            });
            localStorage.setItem("userId", response.data.userId);
            localStorage.setItem("firstName", response.data.firstName);
            localStorage.setItem("lastName", response.data.lastName);
            localStorage.setItem("shelterName", response.data.shelterName);
            localStorage.setItem("userType", response.data.userType);
            navigate("/loggedinuser");
        } catch (error) {
            setError(error.response?.data?.message || "Something went wrong");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            {error && <p>{error}</p>}
            <div className="px-10 font-display bg-white bg-opacity-80">
                <h2 className="text-center text-2xl text-orange font-bold p-10">Zaloguj</h2>
                <form onSubmit={handleSubmit}>
                    <div className="block w-80 m-auto">
                        <div className="px-3">
                            <label htmlFor="email"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">Email:</label>
                            <input
                                type="email"
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                id="email" value={email} onChange={handleEmailChange}/>
                        </div>
                        <div className="px-3">
                            <label htmlFor="password"
                                   className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">Hasło:</label>
                            <input type="password"
                                   className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                   id="password" value={password} onChange={handlePasswordChange}/>
                        </div>
                        <button type="submit"
                                className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown mb-10"
                                disabled={loading}>
                            <p className="py-15 justify-center text-base text-center text-brown font-medium	">Zaloguj</p>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;