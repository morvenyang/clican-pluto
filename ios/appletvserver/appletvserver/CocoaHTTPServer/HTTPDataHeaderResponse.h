//
//  HTTPDataHeaderResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import <Foundation/Foundation.h>
#import "HTTPResponse.h"


@interface HTTPDataHeaderResponse : NSObject <HTTPResponse>
{
	NSUInteger offset;
	NSData *data;
    NSMutableDictionary* headers;
    NSInteger status;
    UInt64 length;
}

- (id)initWithData:(NSData *)data;
- (id)initWithData:(NSData *)data status:(NSInteger)statusParam;
- (id)initWithData:(NSData *)data status:(NSInteger)statusParam length:(UInt64)lengthParam;
@end
