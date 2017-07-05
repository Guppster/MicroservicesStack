#![deny(warnings)]
extern crate futures;
extern crate hyper;
extern crate pretty_env_logger;
extern crate rustc_serialize;

use futures::future::FutureResult;

use hyper::{Get, Post, StatusCode};
use hyper::header::ContentLength;
use hyper::server::{Http, Service, Request, Response};
use rustc_serialize::json;

static INDEX: &'static [u8] = b"Try POST /echo";

#[derive(RustcEncodable, RustcDecodable)]
struct Greeting
{
    msg: String
}

struct Core;

impl Service for Core 
{
    type Request = Request;
    type Response = Response;
    type Error = hyper::Error;
    type Future = FutureResult<Response, hyper::Error>;

    fn call(&self, req: Request) -> Self::Future 
    {
        futures::future::ok(match (req.method(), req.path()) 
                            {
                                (&Get, "/") | (&Get, "/echo") => 
                                {
                                    Response::new()
                                        .with_header(ContentLength(INDEX.len() as u64))
                                        .with_body(INDEX)
                                },

                                (&Post, "/echo") => 
                                {
                                    let mut res = Response::new();

                                    if let Some(len) = req.headers().get::<ContentLength>() 
                                    {
                                        res.headers_mut().set(len.clone());
                                    }

                                    let payload = req.body.read_to_string();
                                    let request: Greeting = json::decode(payload).unwrap();
                                    let greeting = Greeting { msg : request.msg };
                                    let payload = json::encode(&greeting).unwrap();

                                    res.with_body(payload)
                                },

                                _ => 
                                {
                                    Response::new()
                                        .with_status(StatusCode::NotFound)
                                }
                            })
    }
}


fn main() 
{
    pretty_env_logger::init().unwrap();
    let address = "0.0.0.0:8080".parse().unwrap();

    let server = Http::new().bind(&address, || Ok(Core)).unwrap();
    println!("Listening on http://{} with 1 thread.", server.local_addr().unwrap());
    server.run().unwrap();
}
