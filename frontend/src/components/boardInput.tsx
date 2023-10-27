"use client"

import {useState} from 'react';
import Button from '@mui/material/Button';
import Loading from './loading';
import FetchWithAuth from '@/app/utils/fetchWithAuth';

const BoardInput: React.FC<{handleClose: () => void, reqMethod: string, reqUrl: string}> = ({handleClose, reqMethod, reqUrl}) => {
    const [content, setContent] = useState('');
    const [loading, setLoading] = useState(false);
    
    const contentChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setContent(event.target.value);
    }

    const handleSubmit = async () => {
        if(content.trim().length === 0) return;

        setLoading(true);
        await FetchWithAuth(reqUrl, {method: reqMethod, body: JSON.stringify({content: content})})
        .then(res => {
            console.log("res 값")
            console.log(res);
        }).catch(err => {
            console.log("err 발생");
            console.log(err);
        }).finally(() => {
            setLoading(false);
            window.alert("건의사항이 제출되었습니다!!");
        }); 
    }

    return(
        <>
            <Loading open={loading} />
            <div className="mb-4 h-52 ">
                <p className="text-xl font-bold mb-2">내용</p>
                <textarea onChange={contentChange} className="w-full p-2 border rounded h-40" />
            </div>
            <div className="flex justify-end space-x-4">
                <Button onClick={handleSubmit} className="bg-blue-500 text-white px-4 py-2 rounded">제출</Button>
                <Button onClick={handleClose} className="bg-gray-500 text-white px-4 py-2 rounded">취소</Button>
            </div>
        </>
    )
}

export default BoardInput;