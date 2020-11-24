#!/usr/bin/env groovy


  def int indexOf(byte[] array, byte[] target) {
    // checkNotNull(array, "array");
    // checkNotNull(target, "target");
    if (target.length == 0) {
      return 0;
    }

    outer:
    for (int i = 0; i < array.length - target.length + 1; i++) {
      for (int j = 0; j < target.length; j++) {
        if (array[i + j] != target[j]) {
          continue outer;
        }
      }
      return i;
    }
    return -1;
  }


// POST
def post = new URL("http://localhost:8090/hello").openConnection();
def message = '''{"message":"this is a message"}'''
post.setRequestMethod("POST")
post.setDoOutput(true)
post.setRequestProperty("Content-Type", "application/json")
post.getOutputStream().write(message.getBytes("UTF-8"));
def postRC = post.getResponseCode();
println(postRC);
if(postRC.equals(200)) {
    // println(post.getInputStream().getText());

    def in = post.getInputStream();
    int size = 0;
    byte[] buffer = new byte[1024];
    byte[] error = '#ERROR#'.getBytes();
    while ((size = in.read(buffer)) != -1) {
        System.out.write(buffer, 0, size);
        if (indexOf(buffer, error) != -1) {
            throw new Error('出错了')
        }
        // if (new String(buffer).contains('#ERROR#')) {
        //     throw new Error('出错了')
        // }
    }
    in.close()
}




// println 'good'