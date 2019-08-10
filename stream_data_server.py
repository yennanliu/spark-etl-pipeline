from flask import Flask, stream_with_context, request, Response, flash                                                                                                                         
from time import sleep                                                                                                                                                                         

app = Flask(__name__)                                                                                                                                                                          

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

@app.route('/stream')                                                                                                                                                                          
def stream_data():                                                                                                                                                                             
    rows = generate()                                                                                                                                                                          
    return Response(stream_with_context(rows))     
                                                                                                                        
if __name__ == '__main__':   
    app.run(host='0.0.0.0', port=9999, debug=True)                                                                                                                                                                  
