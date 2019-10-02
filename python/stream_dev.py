from flask import Flask, stream_with_context, request, Response, flash                                                                                                                         
from time import sleep 
import requests
import socket                                                                                                                                                                        

app = Flask(__name__)                                                                                                                                                                          

TARGET_HOST = "0.0.0.0"
TARGET_PORT = 9999 

def stream_template(template_name, **context):                                                                                                                                                 
    app.update_template_context(context)                                                                                                                                                       
    t = app.jinja_env.get_template(template_name)                                                                                                                                              
    rv = t.stream(context)                                                                                                                                                                     
    rv.disable_buffering()                                                                                                                                                                     
    return rv                                                                                                                                                                                 

def generate(): 
    data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0]                                                                                                                                                                                
    for item in data:                                                                                                                                                                          
        yield str(item)                                                                                                                                                                        
        sleep(1)                                                                                                                                                                               

@app.route('/stream_page')                                                                                                                                                                          
def stream_view():                                                                                                                                                                             
    rows = generate()                                                                                                                                                                          
    return Response(stream_template('template.html', rows=rows)) 

# @app.route('/')
# def streamed_response():
#     def generate():
#         for i in range(10):
#             yield str('hello world')
#             yield str(i)
#             sleep(1)
#     return Response(stream_with_context(generate())) 

@app.route('/')
def streamed_response():
    def generate():
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client.connect((TARGET_HOST, TARGET_PORT))
        for i in range(50):
            client.send(i)
            client.send("Hello world!")
            response = client.recv(4096)
            print(response)
            return str(i)
    response = generate()
    return Response(stream_template('template.html'), response) 

@app.route('/stream')                                                                                                                                                                          
def stream_data():                                                                                                                                                                            
    rows = generate()                                                                                                                                                                        
    return Response(stream_with_context(generate()))   

if __name__ == '__main__':   
    app.run(host='0.0.0.0', port=9999, debug=True)                                                                                                                                                                  
