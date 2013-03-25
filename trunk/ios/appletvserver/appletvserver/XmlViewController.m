//
//  XmlViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "XmlViewController.h"
#import "TouchXML.h"


@implementation XmlViewController

@synthesize xml = _xml;
@synthesize type = _type;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(id) initWithXml:(NSString*) xml{
    self = [super init];
    if(self){
        self.xml = xml;
    }
    return self;
}
- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_xml);
    [super dealloc];
}
-(void) loadView{
    [super loadView];
    NSError *theError = NULL;
    CXMLDocument *document = [[[CXMLDocument alloc] initWithXMLString:self.xml options:0 error:&theError] autorelease];
    CXMLElement* rootElement=[document rootElement];
    CXMLElement* bodyElement = [[rootElement elementsForName:@"body"] objectAtIndex:0];
    for(int i=0;i<[bodyElement childCount];i++){
        CXMLNode* node = [bodyElement childAtIndex:i];
        if([[node name] isEqualToString:@"listScrollerSplit"]){
            CXMLNode* listScrollerSplit = node;
            self.type = @"listScrollerSplit";
            
        }else{
            
        }
    }
    
}
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
