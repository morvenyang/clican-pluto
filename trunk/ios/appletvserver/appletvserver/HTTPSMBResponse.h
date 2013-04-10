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
    NSString* _smbPath;
    UInt64 _length;
    UInt64 _offset;
    KxSMBItemFile* _smbFile;
    NSMutableDictionary* _headers;
    BOOL _reseek;
}
@property(nonatomic,retain) NSString* smbPath;

@property(nonatomic,retain) KxSMBItemFile* smbFile;
@property(nonatomic,retain) NSMutableDictionary* headers;
-(id)initWithSmbPath:(NSString*)smbPath;
@end
