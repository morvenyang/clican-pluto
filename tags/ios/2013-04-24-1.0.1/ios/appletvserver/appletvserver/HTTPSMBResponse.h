//
//  HTTPSMBResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-4-10.
//
//

#import <Foundation/Foundation.h>
#import "HTTPResponse.h"
#import "KxSMBProvider.h"
@interface HTTPSMBResponse : NSObject<HTTPResponse>{
    UInt64 _length;
    UInt64 _offset;
    KxSMBItemFile* _smbFile;
    NSMutableDictionary* _headers;
    BOOL _reseek;
}
-(id)initWithSmbFile:(KxSMBItemFile*)smbFile;
@end
