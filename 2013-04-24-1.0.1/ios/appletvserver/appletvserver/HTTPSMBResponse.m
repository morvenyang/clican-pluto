//
//  HTTPSMBResponse.m
//  appletvserver
//
//  Created by zhang wei on 13-4-10.
//
//

#import "HTTPSMBResponse.h"

@implementation HTTPSMBResponse

-(id)initWithSmbFile:(KxSMBItemFile*)smbFile{
    self = [super init];
    if(self){
        _smbFile = smbFile;
        _length = (UInt64)_smbFile.stat.size;
        _headers = [NSMutableDictionary dictionary];
    }
    return self;
}

- (UInt64)contentLength
{
	return _length;
}

- (UInt64)offset
{
	return _offset;
}
- (void)setOffset:(UInt64)offsetParam
{
	_offset = offsetParam;
    _reseek = YES;
}

- (NSData *)readDataOfLength:(NSUInteger)lengthParameter
{
    @try {
        if(_reseek){
            [_smbFile seekToFileOffset:_offset whence:SEEK_SET];
            _reseek = NO;
        }
        
        
        NSUInteger remaining = _length - _offset;
        NSUInteger length = lengthParameter < remaining ? lengthParameter : remaining;
        NSData* data = [_smbFile readDataOfLength:length];
        _offset += length;
        return data;
    }
    @catch (NSException *exception) {
        return nil;
    }
   
	
}

- (BOOL)isDone
{
	BOOL result = (_offset == _length);
	
	return result;
}

- (NSInteger)status{
    return 200;
}
- (BOOL)delayResponseHeaders{
    return NO;
}
/**
 * If you want to add any extra HTTP headers to the response,
 * simply return them in a dictionary in this method.
 **/
- (NSDictionary *)httpHeaders{
    return _headers;
}

/**
 * If you don't know the content-length in advance,
 * implement this method in your custom response class and return YES.
 *
 * Important: You should read the discussion at the bottom of this header.
 **/
- (BOOL)isChunked{
    return NO;
}

/**
 * This method is called from the HTTPConnection class when the connection is closed,
 * or when the connection is finished with the response.
 * If your response is asynchronous, you should implement this method so you know not to
 * invoke any methods on the HTTPConnection after this method is called (as the connection may be deallocated).
 **/
- (void)connectionDidClose{
    
}
@end
