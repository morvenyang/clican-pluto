//
//  HTTPMediaFileResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-6-13.
//
//

#import "HTTPFileResponse.h"

@interface HTTPMediaFileResponse : HTTPFileResponse{
    NSMutableDictionary* headers;
}

@end
