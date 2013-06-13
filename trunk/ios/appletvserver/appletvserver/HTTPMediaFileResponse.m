//
//  HTTPMediaFileResponse.m
//  appletvserver
//
//  Created by zhang wei on 13-6-13.
//
//

#import "HTTPMediaFileResponse.h"

@implementation HTTPMediaFileResponse

- (id)initWithFilePath:(NSString *)fpath forConnection:(HTTPConnection *)parent{
    self = [super initWithFilePath:fpath forConnection:parent];
    if(self){
        headers = [NSMutableDictionary dictionary];
    }
    return self;
}

- (NSDictionary *)httpHeaders{
    return headers;
}
@end
