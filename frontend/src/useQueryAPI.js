import { useState, useEffect } from "react";

const useQueryAPI = (term) => {
    const [data, setData] = useState([]);
    const [suggestions, setSuggestions] = useState([]);

    useEffect(() => {
        fetch(`/api/v1/query?q=${term}`, {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                mode: "no-cors",
            },
        })
            .then((response) => response.json())
            .then((result) => setData(result));
        fetch(`/api/v1/suggestions?t=${term}`, {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                mode: "no-cors",
            },
        })
            .then((response) => response.json())
            .then((result) => setSuggestions(result));
    }, [term]);

    return {
        data: data,
        suggestions: suggestions,
    };
};

export default useQueryAPI;
