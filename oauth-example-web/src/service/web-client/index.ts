import axios from "axios"
import datetime from "@/libraries/datetime"

const webClient = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  timeout: datetime.duration({ seconds: 10 }).asMilliseconds(),
})

export default webClient
